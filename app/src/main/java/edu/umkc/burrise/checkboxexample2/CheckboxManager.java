package edu.umkc.burrise.checkboxexample2;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

public class CheckboxManager implements Serializable {
    public static final String SAVE_FILE_NAME = "saveFileName.ser";
    private static CheckboxManager checkboxMgr;
    private boolean checked;
    private transient Context context;

    public static CheckboxManager get(Context context) {
        if (checkboxMgr == null) {

            // first check to see if there is a saved CheckboxManager
            FileInputStream fis = null;
            try {
                fis = context.openFileInput(SAVE_FILE_NAME);
                Log.i("Persistent Checkbox", "found file");

                ObjectInputStream is = new ObjectInputStream(fis);
                checkboxMgr = (CheckboxManager) is.readObject();
                // Since context isn't serialized we need to set it here.
                checkboxMgr.context = context;
                is.close();
                fis.close();
            } catch (FileNotFoundException e) {
                // File not created yet. Create new instance
                checkboxMgr = new CheckboxManager();
                checkboxMgr.context = context;
            } catch (ClassNotFoundException e) {
                deletefile(context);
                e.printStackTrace();
            } catch (OptionalDataException e) {
                deletefile(context);
                e.printStackTrace();
            } catch (StreamCorruptedException e) {
                deletefile(context);
                e.printStackTrace();
            } catch (IOException e) {
                deletefile(context);
                e.printStackTrace();
            }
        }
        return checkboxMgr;
    }

    private static void deletefile(Context context) {
        File dir = context.getFilesDir();
        File file = new File(dir, SAVE_FILE_NAME);
        boolean deleted = file.delete();
    }

    private CheckboxManager() {
        checked = false;
    }

    public void persist() {
        try {
            FileOutputStream fos = context.openFileOutput(SAVE_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setValue(boolean b) {
        checked = b;
    }

    public boolean isChecked() {
        return checked;
    }
}
