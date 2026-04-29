package ru.itmo.client.FileHandler;

import ru.itmo.lab.common.interfaces.IO_Handler;

import java.util.ArrayList;
import java.util.List;

public class ScriptHandler //implements IO_Handler
{
    private String fileName;
    private static List<String> openedFiles = new ArrayList<>();
    private String scriptArg;

    public ScriptHandler()
    {}


//    @Override
//    public void execute( IO_Handler consol )
//    {
//        List<String> commands = new GroupsFileManager( fileName ).loadScript();
//        openedFiles.add( fileName );
//        consol.printInfo("Обнаружено " + commands.size() + "строк в скрипте");
//        consol.printInfo(" --- Выполнение скрипта ---");
//
//        Invoker scriptInvoker = new Invoker( collection );
//        scriptInvoker.initIOput( consol );
//        for(String command : commands)
//        {
//            if(command.trim().startsWith("execute_script"))
//            {
//                scriptArg = command.substring(15).trim();
//                try
//                {
//                    Path newPath = Paths.get(scriptArg).toAbsolutePath().normalize();
//                    for( String openedFile : openedFiles )
//                    {
//                        Path openedFilePath = Paths.get(openedFile).toAbsolutePath().normalize();
//                        if( Files.isSameFile(openedFilePath, newPath) )
//                        {
//                            consol.printError("обнаружена попытка рекурсивного запуска скрипта '" + scriptArg + "'");
//                            openedFiles.remove(fileName);
//                            return;
//                        }
//                    }
//                }
//                catch( IOException e )
//                {
//                    throw new FileManagerException("Возникла ошибка при проверке пути скрипта '" + scriptArg + "'");
//                }
//            }
//
//            scriptInvoker.executeCommand( command );
//        }
//        openedFiles.remove(fileName);
//        consol.printInfo(" --- Скрипт '" + fileName + "' выполнен ---");
//    }

}

