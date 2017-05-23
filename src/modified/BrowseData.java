/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modified;

import java.io.File;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lemma
 */
public class BrowseData {
    
    
       
        
        
    public String[] getFileUrl(String path) {
            try {
                URI uri = Thread.currentThread().getContextClassLoader().getResource(path).toURI();
                File[] files = new File(uri).listFiles();
                
                String[] contents = new String[files.length];
                
                for (int i = 0; i < files.length; i++) {
                    byte[] data = Files.readAllBytes(Paths.get(files[i].toURI()));
                    contents[i] = new String(data, Charset.defaultCharset());
                }
                return contents;
            } catch (Exception ex) {
                Logger.getLogger(BrowseData.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
    }
    
     
    

}
