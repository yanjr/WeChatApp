package com.erdan.wechat.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erdan.wechat.common.ApiTools;
import com.erdan.wechat.wxtools.WeiXinTools;
import com.erdan.wechat.wxtools.vo.recv.WxRecvEventMsg;
import com.erdan.wechat.wxtools.vo.recv.WxRecvGeoMsg;
import com.erdan.wechat.wxtools.vo.recv.WxRecvMsg;
import com.erdan.wechat.wxtools.vo.recv.WxRecvPicMsg;
import com.erdan.wechat.wxtools.vo.recv.WxRecvTextMsg;
import com.erdan.wechat.wxtools.vo.recv.WxRecvVoiceMsg;
import com.erdan.wechat.wxtools.vo.send.*;

/**
 * 微信消息处理 请求地址 http://域名/weixin.do
 */
public class IndexServletAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// token标识
	private static final String TOKEN = "weixin";

	/**
	 * post请求接受用户输入的消息，和消息回复
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			WxRecvMsg msg = WeiXinTools.recv(request.getInputStream());
			WxSendMsg sendMsg = WeiXinTools.builderSendByRecv(msg);

			/** -------------------1.接受到的文本消息，回复处理-------------------------- */
			if (msg instanceof WxRecvTextMsg) {
				WxRecvTextMsg recvMsg = (WxRecvTextMsg) msg;
				// 用户输入的内容
				String text = recvMsg.getContent().trim();

				
				/** ----------- 消息回复示例：文字回复、单(多)图文回复、音乐回复 begin------------- */
				if (text.equals("笑话") || text.equals("1")) {
					// 文本消息回复
					sendMsg = new WxSendTextMsg(sendMsg, ApiTools.jokeApi());
					WeiXinTools.send(sendMsg, response.getOutputStream());
					return;
				} 
				
				else if (text.equals("段子") || text.equals("2")) {
					// 文本消息回复
					sendMsg = new WxSendTextMsg(sendMsg, ApiTools.duanziApi());
					WeiXinTools.send(sendMsg, response.getOutputStream());
					return;
				} 
				
				else if (text.equals("图文")) {
					sendMsg = new WxSendNewsMsg(sendMsg).addItem("图文标题", "图文描述", "http://www.baidu.com/img/bdlogo.gif", "http://www.baidu.com/");
					
					// 多图文内容 --示例
					// .addItem("图文标题", "图文描述" ,
					// "http://www.baidu.com/img/bdlogo.gif",
					// "http://www.baidu.com/");
					WeiXinTools.send(sendMsg, response.getOutputStream());
					return;
				} 
				
				
				else if (text.equals("音乐")) {
					sendMsg = new WxSendMusicMsg(sendMsg, "音乐消息测试", "音乐消息测试内容", "音乐地址.mp3", "高质量音乐地址.mp3");
					WeiXinTools.send(sendMsg, response.getOutputStream());
					return;
				} else {
					// 文本消息回复
					sendMsg = new WxSendTextMsg(sendMsg, "您发的内容是：" + text);
					WeiXinTools.send(sendMsg, response.getOutputStream());
					return;
				}
				
				
				/** ----------- 消息回复示例：文字回复、单(多)图文回复、音乐回复 end ------------- */
				
				
			}

			/** -------------------2.接受到的事件消息-------------------------- */
			else if (msg instanceof WxRecvEventMsg) {
				WxRecvEventMsg recvMsg = (WxRecvEventMsg) msg;
				String event = recvMsg.getEvent();

				if ("subscribe".equals(event)) {
					// 订阅消息
					sendMsg = new WxSendTextMsg(sendMsg, "谢谢您的订阅。");
					WeiXinTools.send(sendMsg, response.getOutputStream());
					return;
				} else if ("unsubscribe".equals(event)) {
					// 取消订阅

					return;

				} else if ("CLICK".equals(event)) {
					// 自定义菜单点击事件
					String eventKey = recvMsg.getEventKey();

					// 判断自定义菜单中的key回复消息
					if ("自定义菜单中的key".equals(eventKey)) {

						return;
					}
				} else {
					// 无法识别的事件消息
					return;
				}

			}

			/** -------------------3.接受到的地理位置信息-------------------------- */
			else if (msg instanceof WxRecvGeoMsg) {
				WxRecvGeoMsg recvMsg = (WxRecvGeoMsg) msg;

				return;
			}

			/** -------------------4.接受到的音频消息-------------------------- */
			else if (msg instanceof WxRecvVoiceMsg) {
				WxRecvVoiceMsg recvMsg = (WxRecvVoiceMsg) msg;

				return;
			}

			/** -------------------5.接受到的图片消息-------------------------- */
			else if (msg instanceof WxRecvPicMsg) {
				WxRecvPicMsg recvMsg = (WxRecvPicMsg) msg;

				return;
			}

			/** ------------------6.接受到的未能识别的消息-------------------- */
			else {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * get请求进行验证服务器是否正常
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 进行接口验证
		 */
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		if (null != timestamp && null != nonce && null != echostr && null != signature) {
			if (WeiXinTools.access(TOKEN, signature, timestamp, nonce)) {
				response.getWriter().write(echostr);
				return;
			}
			return;
		} else {
			return;
		}
	}

}
