package net.xdob.cmd4j.impl;


import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import com.ls.luava.common.Finder;
import com.ls.luava.common.Types;
import net.xdob.cmd4j.service.*;
import net.xdob.cmd4j.annotation.Cmd4jArg;
import net.xdob.cmd4j.annotation.Cmd4jCmd;
import net.xdob.cmd4j.annotation.Cmd4jOption;
import net.xdob.cmd4j.completer.*;
import net.xdob.cmd4j.model.*;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.impl.completer.EnumCompleter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author yangzj
 * @date 2021/7/19
 */
public class CmdSupportService implements CmdSupport {
  public static final Splitter SPLITTER = Splitter.on(" ").trimResults().omitEmptyStrings();
  static final Logger LOG = LoggerFactory.getLogger(CmdSupportService.class);
  public static final String BLANK = " ";
  private final List<Cmd> cmds = new ArrayList<>();
  private LineReader lineReader;
  private ProxyCompleter proxyCompleter;

  private final List<ValuesCompleter> completers = new ArrayList<>();


  private final ThreadLocal<CmdContext> contextThreadLocal = new ThreadLocal<>();

  final ExecutorService executorService = new ThreadPoolExecutor(1, 3,
    0L, TimeUnit.MILLISECONDS,
    new LinkedBlockingQueue<Runnable>());


  private AppContext appContext;

  public CmdSupportService(AppContext appContext, List<ValuesGetterRegister> valuesGetterRegisters, List<Cmd> cmdList) {
    this.appContext = appContext;
    this.lineReader = lineReader;
    cmds.addAll(cmdList);
    completers.add(new ValuesCompleter(Cmd.CMD,v->{
      for (Cmd cmd : getCmds()) {
        v.add(cmd.name());
      }
    }));
    if(valuesGetterRegisters !=null){
      for (ValuesGetterRegister valuesGetterRegister : valuesGetterRegisters) {
        completers.add(new ValuesCompleter(valuesGetterRegister.getName(), valuesGetterRegister));
      }
    }

  }

  @Override
  public LineReader getLineReader() {
    return lineReader;
  }

  @Override
  public void setLineReader(LineReader lineReader) {
    this.lineReader = lineReader;
  }

  @Override
  public String getPrompt() {
    String prompt = "mnode>";

    return prompt;
  }

  @Override
  public ProxyCompleter getProxyCompleter() {
    return proxyCompleter;
  }

  @Override
  public void setProxyCompleter(ProxyCompleter completer) {
    proxyCompleter = completer;
  }

  @Override
  public Completer getCmdCompleter() {
    return getValuesCompleter(Cmd.CMD);
  }

  @Override
  public ThreadLocal<CmdContext> getContextThreadLocal() {
    return contextThreadLocal;
  }

  @Override
  public void handle(String line) {
    final Finder finder = Finder.c(line);
    String name = finder.head(BLANK, f->f).getValue();
    if(name!=null&&!name.isEmpty()) {
      Cmd cmd = getCmds().stream().filter(c -> c.getAllNames().contains(name)).findFirst().orElse(null);
      if (cmd != null) {
        try {
          List<Option> opts = Lists.newArrayList();
          List<String> args = Lists.newArrayList();
          String argsLine = finder.tail(BLANK).getValue();
          if (argsLine != null) {
            List<String> list = SPLITTER.splitToList(argsLine);
            for (String v : list) {
              if (v.startsWith("-")) {
                if (v.startsWith("--")) {
                  Finder vf = Finder.c(v).tail("--");
                  opts.add(Option.of(vf.head("=", f -> f).getValue(), vf.tail("=").getValue()));
                } else {
                  Finder vf = Finder.c(v).tail("-");
                  opts.add(Option.of(vf.head("=", f -> f).getValue(), vf.tail("=").getValue()));
                }
              } else {
                args.add(v);
              }
            }
          }
          int index = 0;
          Field[] fields = cmd.getClass().getDeclaredFields();
          for (Field field : fields) {
            final Cmd4jArg arg = field.getAnnotation(Cmd4jArg.class);
            if (arg != null) {
              final Class<?> type = field.getType();
              Object value = null;
              if (index < args.size()) {
                value = args.get(index);
              }
              if (value != null) {
                value = value.toString().trim();
                if (value.toString().isEmpty()) {
                  value = null;
                }
              }
              if (value == null) {
                value = arg.value();
              }
              index += 1;
              field.setAccessible(true);
              value = Types.cast(value, type);
              field.set(cmd, value);
            }
            final Cmd4jOption annOpt = field.getAnnotation(Cmd4jOption.class);
            if (annOpt != null) {
              final Class<?> type = field.getType();
              List<String> names = Lists.newArrayList();
              names.add(field.getName());
              if (!Strings.isNullOrEmpty(annOpt.shortOption())) {
                names.add(annOpt.shortOption());
              }
              field.setAccessible(true);
              Option opt = getOption(opts, names);
              if (opt != null) {
                Object value = null;
                if (opt.getValue() == null) {
                  value = annOpt.value();
                } else {
                  value = opt.getValue();
                }
                value = Types.cast(value, type);
                field.set(cmd, value);
              } else {
                Object value = annOpt.value();
                value = Types.cast(value, type);
                field.set(cmd, value);
              }
            }
          }
          CmdContext context = new CmdContextImpl(executorService, this, cmd, appContext);
          Cmd4jOut cmd4JOut = context.newT4mOut();
          contextThreadLocal.set(context);
          try {
            cmd.preCmd(context);
            cmd.doCmd(context);
            cmd.postCmd(context);
          } catch (Exception e) {
            cmd4JOut.println(cmd.name() + " Command aborted.", OutColor.RED);
            LOG.warn("doCmd is error", e);
            proxyCompleter.reset();
          }
          contextThreadLocal.set(null);
        } catch (Exception e) {
          System.err.println("arg is error :" + e.getMessage());
          LOG.warn("arg is error", e);
          proxyCompleter.reset();
        }
      } else {
        System.out.println("unknown command :" + line);
      }
    }
  }

  private Option getOption(List<Option> opts, List<String> names) {
    Option opt= opts.stream()
        .filter(e->names.contains(e.getName())).findFirst().orElse(null);
    return opt;
  }

  @Override
  public List<CmdHelper> getHelper(String cmd, String space) {
    List<CmdHelper> helpers = new ArrayList<>();
    if(!Strings.isNullOrEmpty(cmd)){
      List<Cmd> cmdList = getCmds().stream().filter(c->c.getAllNames().contains(cmd)).collect(Collectors.toList());
      for (Cmd cmd2 : cmdList) {
        helpers.add(buildHelper(cmd2));
      }
    }else{
      List<Cmd> cmdList = getCmds();
      for (Cmd cmd2 : cmdList) {
        helpers.add(buildHelper(cmd2));
      }
    }
    return helpers;
  }

  @Override
  public List<Cmd> getCmds() {
    return cmds.stream().filter(e->e.space().contains(appContext.getSpace())||e.space().contains(Cmd.SPACE_ALL))
        .collect(Collectors.toList());
  }

  @Override
  public Cmd getCmd(String name) {
    return getCmds().stream().filter(e->e.name().equals(name)).findFirst().orElse(null);
  }

  @Override
  public List<CmdArg> getCmdArgs(Cmd cmd) {
    List<CmdArg> cmdArgs = Lists.newArrayList();
    Field[] fields = cmd.getClass().getDeclaredFields();
    for (Field field : fields) {
      final Cmd4jArg annArg = field.getAnnotation(Cmd4jArg.class);
      if (annArg != null) {
        CmdArg cmdArg = new CmdArg(field.getName());
        final Class<?> type = field.getType();
        cmdArg.setType(type);
        cmdArg.setValue(annArg.value());
        cmdArg.setValues(annArg.values());
        cmdArg.setRequired(annArg.required());
        cmdArg.setForced(annArg.forced());
        cmdArg.setDesc(annArg.desc());
        cmdArg.setDynamic(annArg.dynamic());
        if(cmdArg.getType()==Integer.class||cmdArg.getType()==int.class){
          cmdArg.getCompleter().setCompleter(new IntCompleter().setMin(annArg.min()).setMax(annArg.max()));
        }else if(Enum.class.isAssignableFrom(cmdArg.getType())){
          cmdArg.getCompleter().setCompleter(new EnumCompleter((Class<? extends Enum<?>>)cmdArg.getType()));
        }else if(cmdArg.hasValues()){
          cmdArg.getCompleter().setCompleter(new StringsCompleter2(cmdArg.getValues()).setForced(cmdArg.isForced()));
        } else {
          if(!Strings.isNullOrEmpty(cmdArg.getDynamic())){
            ValuesCompleter valuesCompleter = getValuesCompleter(cmdArg.getDynamic());
            cmdArg.getCompleter().setCompleter(valuesCompleter);
          }
        }
        cmdArgs.add(cmdArg);
      }
    }
    return cmdArgs;
  }

  @Override
  public List<CmdArg> getCmdArgs(String name) {
    List<CmdArg> cmdArgs = Lists.newArrayList();
    Cmd cmd = getCmd(name);
    if(cmd!=null){
      cmdArgs.addAll(getCmdArgs(cmd));
    }
    return cmdArgs;
  }

  @Override
  public List<CmdOption> getAllOptions(Cmd cmd) {
    List<CmdOption> options = Lists.newArrayList();
    Field[] fields = cmd.getClass().getDeclaredFields();
    for (Field field : fields) {
      final Cmd4jOption annOpt = field.getAnnotation(Cmd4jOption.class);
      if(annOpt!=null){
        String shortOption = Strings.emptyToNull(annOpt.shortOption());
        CmdOption opt = new CmdOption(field.getName(), shortOption);
        opt.setDesc(annOpt.desc());
        opt.setType(field.getType());
        opt.setValue(annOpt.value());
        opt.setValues(annOpt.values());
        opt.setDynamic(annOpt.dynamic());
        if(opt.getType()==Integer.class||opt.getType()==int.class){
          opt.getCompleter().setCompleter(new IntCompleter().setMin(annOpt.min()).setMax(annOpt.max()));
        }else if(Enum.class.isAssignableFrom(opt.getType())){
          opt.getCompleter().setCompleter(new EnumCompleter((Class<? extends Enum<?>>)opt.getType()));
        }else if(opt.hasValues()){
          opt.getCompleter().setCompleter(new StringsCompleter2(opt.getValues()).setForced(opt.isForced()));
        } else {
          if(!Strings.isNullOrEmpty(opt.getDynamic())){
            ValuesCompleter valuesCompleter = getValuesCompleter(opt.getDynamic());
            opt.getCompleter().setCompleter(valuesCompleter);
          }
        }
        options.add(opt);
      }
    }
    return options;
  }

  @Override
  public List<CmdOption> getAllOptions(String name) {
    List<CmdOption> options = Lists.newArrayList();
    Cmd cmd = getCmd(name);
    if(cmd!=null){
      options.addAll(getAllOptions(cmd));
    }
    return options;
  }

  @Override
  public List<Completer> getArgCompleters(String name) {
    List<Completer> completers = Lists.newArrayList();
    Cmd cmd = getCmd(name);
    if(cmd!=null) {
      for (CmdArg cmdArg : getCmdArgs(cmd)) {
        completers.add(cmdArg.getCompleter());
      }
    }
    //completers.add(NullCompleter.INSTANCE);
    return completers;
  }

  CmdHelper buildHelper(Cmd cmd){
    CmdHelper helper = new CmdHelper(cmd.name(),cmd.space());

    final Cmd4jCmd cmd4JCmd = cmd.getClass().getAnnotation(Cmd4jCmd.class);
    helper.setDesc(cmd4JCmd.value());
    final String[] alias = cmd.alias();
    if(alias!=null){
      for (String s : alias) {
        helper.getAlias().add(s);
      }
    }
    for (String eg : cmd4JCmd.eg()) {
      helper.getEg().add(eg);
    }

    List<CmdOption> options = this.getAllOptions(cmd);
    for (CmdOption option : options) {
      OptHelper optHelper = new OptHelper(option.getOption());
      optHelper.setDesc(option.getDesc());
      optHelper.setShortOption(option.getShortOption());
      helper.getOpts().add(optHelper);
    }

    final List<CmdArg> cmdArgs = getCmdArgs(cmd);
    StringBuffer buffer = new StringBuffer(cmd.name());

    for (CmdArg cmdArg : cmdArgs) {
      if(cmdArg.isRequired()){
        buffer.append(" ").append(cmdArg.getName());
      }else {
        buffer.append(" [").append(cmdArg.getName()).append("]");
      }
    }

    helper.setDetail(buffer.toString());

    if(cmdArgs.size()>0) {
      for (CmdArg cmdArg : cmdArgs) {
        ArgHelper argHelper = new ArgHelper(cmdArg.getName());
        argHelper.setDesc(cmdArg.getDesc());
        helper.getArgs().add(argHelper);
      }
    }

    return helper;
  }

  @Override
  public ValuesCompleter getValuesCompleter(String name) {
    ValuesCompleter completer = completers.stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
    return completer;
  }
}
