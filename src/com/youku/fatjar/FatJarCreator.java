package com.youku.fatjar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * @author douyoumi
 * @email xinshou.cb@gmail.com
 * @date 2016-08-18
 */
public class FatJarCreator {
	private final static String ASSETS_PREFIX = "assets/";
	
    /** 
     * @param jars  JAR路径的数组 
     * @param output  新文件的合并路径 
     * @return result
     */  
    public boolean create(ArrayList<String> jarsPath, String assetPath, String output) {
    	boolean result = true;
        Manifest manifest = getManifest();  
  
        FileOutputStream fos = null;
        JarOutputStream jos = null;
		try {
			fos = new FileOutputStream(output);
			jos = new JarOutputStream(fos, manifest);
			addFilesFromJars(jarsPath, jos);  
			if(null != assetPath){
				addAssets(new File(assetPath), ASSETS_PREFIX, jos);
			}else{
				System.out.println("!!!Path of assets is null!!!");
			}
			System.out.println("Create fat jar success");
		} catch (IOException e1) {
			result = false;
			e1.printStackTrace();
		} finally {  
            try {
            	if(null != jos){
            		jos.close();
            	}
            	if(null != fos){
            		fos.close();  
            	}
			} catch (IOException e) {
				e.printStackTrace();
			}  
        }  
        
        return result;  
    }    
  
    private Manifest getManifest() {  
        Manifest manifest = new Manifest();  
        Attributes attribute = manifest.getMainAttributes();  
        attribute.putValue("Manifest-Version", "1.0");  
        attribute.putValue("Created-By","Youku fat jar plugin");  
        return manifest;  
    }  
  
    private void addFilesFromJars(ArrayList<String> jarsPath, JarOutputStream out) throws IOException {
    	String jarPath = "";
        for (int i = 0; i < jarsPath.size(); i++) {
        	jarPath = jarsPath.get(i);
            if(!new File(jarPath).exists()){
            	System.out.println("!!!Create fat jar failed!!!");
            	System.out.println("Can't find "+jarPath);
            	break;
            }
            System.out.println("JarName=" + jarPath);
            
            JarFile jarFile = new JarFile(jarPath);
            Enumeration<?> entities = jarFile.entries();  
  
            while (entities.hasMoreElements()) {  
                JarEntry entry = (JarEntry) entities.nextElement();  
  
                if (entry.isDirectory() || entry.getName().toLowerCase().startsWith("meta-inf")) {  
                    continue;  
                }  

                InputStream in = jarFile.getInputStream(entry);
                copyData2Jar(in, out, entry.getName());
            }  
  
            jarFile.close();  
        }  
    }  
 
    private void addAssets(File dir, String jarEntryPrefix, JarOutputStream out) throws IOException{
   		if(!dir.exists() || !dir.isDirectory()){
   			System.out.println("dir is not exist or is not a directory");
   			return;
   		}
   		
   		File[] files = dir.listFiles();
   		for(File file : files){
   			addAssetFile(file, jarEntryPrefix, out);
   		}
    }
    
   private void addAssetFile(File file, String jarEntryPrefix, JarOutputStream out) throws IOException{
 	   System.out.println("asset file name is "+file.getName());
 	   
	   if (file.exists() && file.isFile()) {  
	       InputStream in = new FileInputStream(file);
	       copyData2Jar(in, out, jarEntryPrefix+file.getName());
	   } else {
		   addAssets(file, jarEntryPrefix+file.getName()+"/", out);
	   }
   }
    
   private void copyData2Jar(InputStream in, JarOutputStream out, String newEntryName) throws IOException{
	   int bufferSize;  
 	   byte[] buffer = new byte[1024]; 
	   
       out.putNextEntry(new JarEntry(newEntryName));  
   	
       while ((bufferSize = in.read(buffer, 0, buffer.length)) != -1) {  
           out.write(buffer, 0, bufferSize);  
       }  

       in.close();  
       out.closeEntry();  
   }
}
