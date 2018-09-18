import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String req = (String) msg;
        System.out.println(req);
        Date dt = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        String body = new String(" this is server res :" + fmt.format(dt) + "  " + req + "\n");
        ByteBuf res = Unpooled.copiedBuffer(body.getBytes("UTF-8"));
        ctx.writeAndFlush(res);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
