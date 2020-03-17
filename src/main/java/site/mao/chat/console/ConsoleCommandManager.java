package site.mao.chat.console;


import io.netty.channel.Channel;

import java.util.*;

public class ConsoleCommandManager implements ConsoleCommand {

    private Map<String,ConsoleCommand> consoleCommandMap;

    public ConsoleCommandManager(){

        consoleCommandMap = new HashMap<>();

        ServiceLoader<ConsoleCommand> consoleCommandServiceLoader
                = ServiceLoader.load(ConsoleCommand.class);

        consoleCommandServiceLoader.forEach((command)->{
            String commandName = command.getCommandName();
            if (Objects.nonNull(commandName)){
                consoleCommandMap.putIfAbsent(commandName,command);
            }
        });

    }

    @Override
    public void exec(Scanner scanner, Channel channel) {

        String command = scanner.nextLine();

        ConsoleCommand consoleCommand = consoleCommandMap.get(command);


        if (consoleCommand != null) {
            consoleCommand.exec(scanner, channel);
        } else if (command.equals("help")) {
            consoleCommandMap.keySet().stream()
                    .forEach(System.out::println);
        } else {
            System.err.println("无法识别[" + command + "]指令，请重新输入!");
        }
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    public static void main(String[] args) {
        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
        Scanner scanner = new Scanner(System.in);
        while (true){
            consoleCommandManager.exec(scanner,null);
        }
    }
}
