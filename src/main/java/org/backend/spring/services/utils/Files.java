package org.backend.spring.services.utils;

import java.io.File;

public class Files {
    public static boolean deleteFileOrDirectory(File file)
    {
        if(file.isDirectory())
        {
            for (File file_in_directory:
                    file.listFiles()) {
                if(!deleteFileOrDirectory(file_in_directory))
                {
                    return false;
                }
            }
        }
        else
        {
            if(!file.exists())
            {
                return true;
            }
            try {
                if(!file.delete())
                {
                    return false;
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                return false;
            }
        }
        return true;
    }
}
