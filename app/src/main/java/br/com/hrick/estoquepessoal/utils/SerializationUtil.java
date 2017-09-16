package br.com.hrick.estoquepessoal.utils;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Meg on 15/09/2017.
 */
public class SerializationUtil {
    /** Read the object from Base64 string. */
    static Object fromString(String s) throws IOException,
            ClassNotFoundException {
        final byte [] data = Base64.decode(s, Base64.DEFAULT);
        final ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        final Object o  = ois.readObject();

        ois.close();

        return o;
    }

    /** Write the object to a Base64 string. */
    static  String toString(Serializable o) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(o);
        oos.close();
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }
}
