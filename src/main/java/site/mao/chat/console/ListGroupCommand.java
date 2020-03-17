package site.mao.chat.console;

import io.netty.channel.Channel;

import java.util.Scanner;

public class ListGroupCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

    }

    @Override
    public String getCommandName() {
        return "lg";
    }
}
