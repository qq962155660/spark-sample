package spark.stream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

	public static void main(String[] args) throws IOException, InterruptedException {
			ServerSocket ss = new ServerSocket(7777);
			System.out.println("����������....");
			Socket s = ss.accept();
			System.out.println("�ͻ���:" + s.getInetAddress().getLocalHost() + "�����ӵ�������");
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				bw.write("aa,bb,cc,aa,aa,bb\n");
				bw.flush();
				
			s.close();
			//Thread.sleep(1000 * 60 * 10);
	}

}