import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

import xyf.frpc.dev.test.interfaces.INoexist;
import xyf.frpc.dev.test.interfaces.ITest;
import xyf.frpc.remoting.codec.netty.NettyResponseCoder;
import xyf.frpc.remoting.data.Head;
import xyf.frpc.remoting.data.RequestBody;
import xyf.frpc.remoting.data.ResponseBody;

public class Client {
	public void connect(int port, String host) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(new TimeClientHandler());
				}
			});
			ChannelFuture f = b.connect(host, port).sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}
	public static void main(String[] args) throws Exception {
		new Client().connect(8080, "127.0.0.1");
//		Method method = INoexist.class.getDeclaredMethod("nomethod", int.class);
//		System.out.println(method.getParameterTypes());
	}

}

class TimeClientHandler extends ChannelInboundHandlerAdapter {
	NettyResponseCoder coder= new NettyResponseCoder();
	public TimeClientHandler() {
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws IOException, NoSuchMethodException, SecurityException {
		ByteBuf msg = null;
		try{
		for(int i = 0; i < 10; i++) {
			Head head = new Head();
			head.setMagic(Head.MAGIC);
			head.setInvokeId(10*i);
			
			RequestBody body = new RequestBody();

			if(i%2 == 0) {
				body.setArguments(new Object[]{"This is arg " + i});
				body.setInterfaceFullName(ITest.class.getName());
				body.setMethodName("fun");
				Method method = ITest.class.getDeclaredMethod("fun", String.class);
				body.setParameterTypes(method.getParameterTypes());
			}
			else {
				body.setArguments(new Object[]{1});
				body.setInterfaceFullName(INoexist.class.getName());
				body.setMethodName("nomethod");
				Method method = INoexist.class.getDeclaredMethod("nomethod", int.class);
				body.setParameterTypes(method.getParameterTypes());
			}
			System.out.println("----Client:" + body.getInterfaceFullName() + " " +body.getMethodName());
			byte [] bodyBytes = JavaSerializableReqRespBodyPack.toArray(body);
			head.setBodyLength(bodyBytes.length);
			
			
			byte [] headBytes = Head.head2Bytes(head);
			
			msg = Unpooled.buffer(headBytes.length + bodyBytes.length);
			
			msg.writeBytes(headBytes);
			msg.writeBytes(bodyBytes);
			
			ctx.writeAndFlush(msg);
		}
		}
		catch(Exception ex) {
			System.out.println("--Client Ex:" + ex.getMessage());
		}
	}
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("client channelRead");
		coder.decode(msg, ctx);
		ctx.read();
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override 
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
}

class JavaSerializableReqRespBodyPack {
	
	public static RequestBody toRequestBodyObject(byte [] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		ObjectInputStream oin = new ObjectInputStream(in);
		RequestBody body = (RequestBody)oin.readObject();
		return body;
	}
	
	public static byte[] toArray(Object body) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oout = new ObjectOutputStream(out);
		oout.writeObject(body);
		return out.toByteArray();
	}
	
	public static ResponseBody toResponseBodyObject(byte [] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		ObjectInputStream oin = new ObjectInputStream(in);
		ResponseBody body = (ResponseBody)oin.readObject();
		return body;
	}
	
	
}
