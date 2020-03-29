/*
 * Copyright (c) 2015 The Jupiter Project
 *
 * Licensed under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jupiter.serialization.msgpack.basic;

import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;

import java.io.IOException;

/**
 * 测试Jupiter的协议结构
 *  * Jupiter传输层协议头
 *  *
 *  * **************************************************************************************************
 *  *                                          Protocol
 *  *  ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
 *  *       2   │   1   │    1   │     8     │      4      │
 *  *  ├ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┤
 *  *           │       │        │           │             │
 *  *  │  MAGIC   Sign    Status   Invoke Id    Body Size                    Body Content              │
 *  *           │       │        │           │             │
 *  *  └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 *  *
 *  * 消息头16个字节定长
 *  * = 2 // magic = (short) 0xbabe
 *  * + 1 // 消息标志位, 低地址4位用来表示消息类型request/response/heartbeat等, 高地址4位用来表示序列化类型
 *  * + 1 // 状态位, 设置请求响应状态
 *  * + 8 // 消息 id, long 类型, 未来jupiter可能将id限制在48位, 留出高地址的16位作为扩展字段
 *  * + 4 // 消息体 body 长度, int 类型
 * @author JackLei
 * @version 2020-03-29
 */
public class TestJupiterMsgpack {

    public static void main(String[] args) throws IOException {
        byte[] bytes = write();
        read(bytes);
    }

    /**
     * Jupiter协议结构
     */
    @Message
    public static class JMsgPack{
        public short magic;
        public byte sign;
        public byte status;
        public long invokeId;
        public int bodySize;
        public byte[] bytes;
    }

    /**
     * 登陆协议
     */
    @Message
    public static class LoginMsgPack{
        public String account;
        public String password;

        @Override
        public String toString() {
            return "LoginMsgPack{" +
                    "account='" + account + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    /**
     * 写入数据
     * @return
     * @throws IOException
     */
    public static byte[] write() throws IOException {
        LoginMsgPack msg = new LoginMsgPack();
        msg.account = "jack";
        msg.password = "123456";

        MessagePack pack  = new MessagePack();
        byte[] bytes = pack.write(msg);

        JMsgPack jmsg = new JMsgPack();
        jmsg.magic = 0xab;


        //高四位表示 序列化类型
        byte serilizaType = 0x06;
        //低四位表示协议类型
        byte protolType = 0x01;

        byte sign = (byte) ((((int)serilizaType & 0xff) << 4) | (protolType & 0x0f));
        jmsg.sign = sign;
        jmsg.status = 0;
        jmsg.invokeId = 1001;
        jmsg.bodySize = bytes.length;
        jmsg.bytes = bytes;


        MessagePack pack2  = new MessagePack();
        byte[] bytes2 = pack2.write(jmsg);
        return bytes2;
    }

    public static void read(byte[] bytes) throws IOException {
        MessagePack pack1 = new MessagePack();
        JMsgPack jMsgPack = pack1.read(bytes, JMsgPack.class);

        MessagePack pack2 = new MessagePack();
        LoginMsgPack msg2 = pack2.read(jMsgPack.bytes, LoginMsgPack.class);
        System.out.println(msg2);
    }

}
