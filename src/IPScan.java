import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;

public class IPScan {

	public static void main(String[] args) {

		System.out.println("Start");
		Socket socket = null;

		try {
			String localIP = InetAddress.getLocalHost().getHostAddress();
			String localHostName = InetAddress.getLocalHost().getHostName();
			String[] ips = localIP.split("\\.");

			System.out.println("IP:" + localIP);// 本机IP
			System.out.println("HostName:" + localHostName);// 本机名称

			InetAddress localHost = InetAddress.getLocalHost();

			NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);

			/*
			 * 子网掩码
			 */
			int prefix = Integer.MAX_VALUE;
			for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
				if (address.getNetworkPrefixLength() < prefix) {

					prefix = address.getNetworkPrefixLength();

				}
				System.out.println(address.getAddress() + "/" + address.getNetworkPrefixLength());

			}
			String subNetMask = getSubNetMask(prefix);
			String[] subnets = subNetMask.split("\\.");

			/*
			 * 计算网络号
			 */
			String[] net = new String[4];

			for (int i = 0; i < 4; i++) {
				net[i] = (Integer.parseInt(ips[i]) & Integer.parseInt(subnets[i])) + "";
			}

			System.out.println("Net:" + net[0] + "." + net[1] + "." + net[2] + "." + net[3]);

			/*
			 * 计算主机ip范围
			 */
			StringBuffer max_net = new StringBuffer(
					toBinary(Integer.parseInt(net[0]), 8) + "." + toBinary(Integer.parseInt(net[1]), 8) + "."
							+ toBinary(Integer.parseInt(net[2]), 8) + "." + toBinary(Integer.parseInt(net[3]), 8));

			for (int i = max_net.length() - 1, j = 0; j < 32 - prefix; i--) {
				if (max_net.charAt(i) == '.') {

					continue;

				}
				max_net.setCharAt(i, '1');
				j++;
			}
			System.out.println(max_net);
			String[] Max_net = (new String(max_net)).split("\\.");
//			System.out.println(Integer.valueOf(Max_net[0],2).toString()
//								+"."+Integer.valueOf(Max_net[1],2).toString()
//								+"."+Integer.valueOf(Max_net[2],2).toString()
//								+"."+Integer.valueOf(Max_net[3],2).toString());

			for (int i = Integer.parseInt(net[0]); i <= Integer.valueOf(Max_net[0], 2); i++)
				for (int j = Integer.parseInt(net[1]); j <= Integer.valueOf(Max_net[1], 2); j++)
					for (int k = Integer.parseInt(net[2]); k <= Integer.valueOf(Max_net[2], 2); k++)
						for (int l = Integer.parseInt(net[3]); l <= Integer.valueOf(Max_net[3], 2); l++) {

							if ((socket = isOpen(i + "." + j + "." + k + "." + l, 135, 300)) != null) {

								System.out.println(socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
							}
						}
			if ((socket != null) && (!socket.isClosed())) {
				socket.close();
			}
			System.out.println("End");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Socket isOpen(String ip, int port, int timeout) {
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(ip, port), timeout);

		} catch (IOException e) {
			return null;
		}
		return socket;
	}

	public static String getSubNetMask(int maskBit) {
		if (1 == maskBit)
			return "128.0.0.0";
		if (2 == maskBit)
			return "192.0.0.0";
		if (3 == maskBit)
			return "224.0.0.0";
		if (4 == maskBit)
			return "240.0.0.0";
		if (5 == maskBit)
			return "248.0.0.0";
		if (6 == maskBit)
			return "252.0.0.0";
		if (7 == maskBit)
			return "254.0.0.0";
		if (8 == maskBit)
			return "255.0.0.0";
		if (9 == maskBit)
			return "255.128.0.0";
		if (10 == maskBit)
			return "255.192.0.0";
		if (11 == maskBit)
			return "255.224.0.0";
		if (12 == maskBit)
			return "255.240.0.0";
		if (13 == maskBit)
			return "255.248.0.0";
		if (14 == maskBit)
			return "255.252.0.0";
		if (15 == maskBit)
			return "255.254.0.0";
		if (16 == maskBit)
			return "255.255.0.0";
		if (17 == maskBit)
			return "255.255.128.0";
		if (18 == maskBit)
			return "255.255.192.0";
		if (19 == maskBit)
			return "255.255.224.0";
		if (20 == maskBit)
			return "255.255.240.0";
		if (21 == maskBit)
			return "255.255.248.0";
		if (22 == maskBit)
			return "255.255.252.0";
		if (23 == maskBit)
			return "255.255.254.0";
		if (24 == maskBit)
			return "255.255.255.0";
		if (25 == maskBit)
			return "255.255.255.128";
		if (26 == maskBit)
			return "255.255.255.192";
		if (27 == maskBit)
			return "255.255.255.224";
		if (28 == maskBit)
			return "255.255.255.240";
		if (29 == maskBit)
			return "255.255.255.248";
		if (30 == maskBit)
			return "255.255.255.252";
		if (31 == maskBit)
			return "255.255.255.254";
		if (32 == maskBit)
			return "255.255.255.255";

		return "-1";
	}

	public static String toBinary(int num, int digits) {
		StringBuffer bi = new StringBuffer(Integer.toBinaryString(num));
		while (bi.length() < digits) {
			bi.insert(0, '0');
		}
		return new String(bi);
	}

}