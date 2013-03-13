package com.senacor.hd12.network.routes;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * Ralph Winzinger, Senacor
 */
@Component
public class VisualizerRoute extends BaseRoute {
    public VisualizerRoute() {
        super("/vis/:file");
    }

    /*
    @Override
    public Object handle(Request request, Response response) {
        String file = request.params(":file");
        System.out.println(file + " requested ... ");

        InputStream is = getClass().getClassLoader().getResourceAsStream(file);
        if (is == null) {
            System.out.println(file + " not found - 404");
            halt(404, "could not find '" + file + "'");
        }


        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            halt(501, "The file is too big");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        try {
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
        } catch (IOException ioe) {
            halt(501, ioe.getMessage());
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            halt(501, "The file was not completely read: " + file);
        }

        // Close the input stream, all file contents are in the bytes variable
        try {
            is.close();
        } catch (IOException ioe) {
            halt(501, ioe.getMessage());
        }

        System.out.println(file + " serving.");

        try {
            response.raw().getOutputStream().write(bytes);
        } catch (IOException ioe) {
            halt(501, ioe.getMessage());
        }
        return "";
    }
    */
}
