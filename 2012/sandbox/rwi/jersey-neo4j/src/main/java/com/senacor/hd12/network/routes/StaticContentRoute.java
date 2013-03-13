package com.senacor.hd12.network.routes;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Ralph Winzinger, Senacor
 */
@Component
public class StaticContentRoute extends BaseRoute {
    public StaticContentRoute() {
        super("/:file");
    }

    /*
    @Override
    public Object handle(Request request, Response response) {
        String file = request.params(":file");
        System.out.println(file + " requested ... ");

        InputStream is = getClass().getClassLoader().getResourceAsStream(file);
        if (is == null) {
            System.out.println(file+" not found - 404");
            halt(404, "could not find '" + file + "'");
        }

        StringBuffer fileContent = new StringBuffer();

        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();

            while (line != null) {
                fileContent.append(line).append("\n");
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println(file+": internal server error - 501");
            halt(501, e.getMessage());
        }

        System.out.println(file+" serving.");

        return fileContent.toString();
    }
    */
}
