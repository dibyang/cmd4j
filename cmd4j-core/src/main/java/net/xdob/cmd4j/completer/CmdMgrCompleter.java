package net.xdob.cmd4j.completer;

import com.google.common.collect.Lists;

import net.xdob.cmd4j.service.Cmd;
import net.xdob.cmd4j.service.CmdSupport;
import net.xdob.cmd4j.model.CmdOption;
import org.jline.builtins.Completers;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class CmdMgrCompleter implements Completer {
  static final Logger LOG = LoggerFactory.getLogger(CmdMgrCompleter.class);
  private final CmdSupport cmdMgr;

  public CmdMgrCompleter(CmdSupport cmdMgr) {
    this.cmdMgr = cmdMgr;
  }

  @Override
  public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
    Objects.requireNonNull(line);
    Objects.requireNonNull(candidates);
    if(line.words().size()<2){
      cmdMgr.getCmdCompleter().complete(reader, line, candidates);
    }else{
      String command = reader.getParser().getCommand(line.words().get(0));
      Cmd cmd = cmdMgr.getCmd(command);
      if(cmd!=null){
        List<Completers.OptDesc> optDescs = Lists.newArrayList();
        for (CmdOption option : cmdMgr.getAllOptions(cmd)) {
          String shortOption = option.getShortOption();
          String longOption = option.getOption();
          Completers.OptDesc optDesc = new Completers.OptDesc(shortOption, longOption);
          optDesc.setValueCompleter(option.getCompleter());
          optDescs.add(optDesc);
        }
        List<Completer> argCompleters = cmdMgr.getArgCompleters(command);
        Completers.OptionCompleter optionCompleter = new Completers.OptionCompleter(argCompleters
            , optDescs
            , 1);
        optionCompleter.complete(reader, line, candidates);
      }
    }

  }
}
