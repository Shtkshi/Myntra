package com.example.myntra;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;

/*
 * Executes a new thread to upload image
 * */
public class T implements Executor {

    @Override
    public void execute(Runnable command) {
        Thread thread = new Thread(command);
        thread.start();
    }

    public static void executeQuery(byte[][] bytes) {
        T obj = new T();
        try {
            obj.execute(new NewThread(bytes));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

/*
 * Creates a new thread
 * */
class NewThread implements Runnable {
    private byte[][] bytes;

    public NewThread(byte[][] bytes) {
        this.bytes = bytes;
    }

    public static String upload(byte[][] byteArray) throws IOException {


        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "Image Upload";

        URL urlObject = new URL("http://192.168.1.240:5000/api/android");
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setUseCaches(false);
        connection.setDoOutput(true);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + boundary);

        DataOutputStream request = new DataOutputStream(connection.getOutputStream());
        for (int i = 0; i < 4; i++) {
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + "image" + i + "\"" + crlf);
            request.writeBytes(crlf);
            request.write(byteArray[i], 0, byteArray[i].length);
            request.writeBytes(crlf);
        }
        request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
        request.flush();
        request.close();

        InputStream responseStream = new BufferedInputStream(connection.getInputStream());

        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

        String line = "";
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = responseStreamReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        responseStreamReader.close();

        String response = stringBuilder.toString();
        // System.out.println(response);
        if (response.contains("\n")) {
            response = response.split("\n")[0];
        }
        responseStream.close();
        connection.disconnect();
        return response;
    }

    @Override
    public void run() {
        try {
            upload(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
