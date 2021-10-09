package pw.saber.shop.utils;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.util.Logger;
import pw.saber.shop.ShopAddon;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

    public static void ExportResource(String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        //String jarFolder;
        try {
            stream = ShopAddon.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if (stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            resStreamOut = new FileOutputStream(FactionsPlugin.getInstance().getDataFolder() + "/configuration/addons" + resourceName);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            Logger.print("Shop File Successfully Transferred!", Logger.PrefixType.DEFAULT);
        } finally {
            stream.close();
            resStreamOut.close();
        }
    }
}
