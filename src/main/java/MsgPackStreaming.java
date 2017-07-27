import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MsgPackStreaming {

    public static void main(String[] args) throws Exception {
        String[] types = {"v1", "v2", "v3", "v4", "v5"};
        double[] data = {0.1, 0.2, 0.3, 0.4, 0.5};
        Server server = new Server(8080);
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                System.out.println(target);
                if (target.startsWith("/msgpack")) {
                    MessagePacker mp = MessagePack.newDefaultPacker(response.getOutputStream());
                    mp.packArrayHeader(6);
                    mp.packString("ts");
                    mp.packString(types[0]);
                    mp.packString(types[1]);
                    mp.packString(types[2]);
                    mp.packString(types[3]);
                    mp.packString(types[4]);
                    mp.flush();

                    for (int i = 0; i < 1000000; i++) {
                        mp.packArrayHeader(6);
                        mp.packString(Integer.toString(i));
                        mp.packDouble(data[0] * i);
                        mp.packDouble(data[1] * i);
                        mp.packDouble(data[2] * i);
                        mp.packDouble(data[3] * i);
                        mp.packDouble(data[4] * i);
                    }
                    mp.close();
                    baseRequest.setHandled(true);
                } else if (target.startsWith("/tsv")) {
                    PrintWriter writer = response.getWriter();
                    writer.write("ts\t");
                    writer.write(types[0]);
                    writer.write( "\t");
                    writer.write(types[1]);
                    writer.write( "\t");
                    writer.write(types[2]);
                    writer.write( "\t");
                    writer.write(types[3]);
                    writer.write( "\t");
                    writer.write(types[4]);
                    writer.write("\n");
                    writer.flush();
                    for (int i = 0; i < 1000000; i++) {
                        writer.write(Integer.toString(i));
                        writer.write("\t");
                        writer.write(Double.toString(data[0] * i));
                        writer.write("\t");
                        writer.write(Double.toString(data[1] * i));
                        writer.write("\t");
                        writer.write(Double.toString(data[2] * i));
                        writer.write("\t");
                        writer.write(Double.toString(data[3] * i));
                        writer.write("\t");
                        writer.write(Double.toString(data[4] * i));
                        writer.write("\n");
                    }
                    writer.close();
                    baseRequest.setHandled(true);
                }
            }
        });

        server.start();
        server.join();
    }
}
