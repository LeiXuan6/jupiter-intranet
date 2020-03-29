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
 *  测试官方例子
 * @author JackLei
 * @version 2020-03-29
 */
public class TestMsgPack {

    @Message
    public static class MyMessage{
        public String name;
        public double version;

        @Override
        public String toString() {
            return "MyMessage{" +
                    "name='" + name + '\'' +
                    ", version=" + version +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException {
        MyMessage message = new MyMessage();
        message.name  = "login";
        message.version = 1.0d;

        MessagePack pack = new MessagePack();
        byte[] bytes = pack.write(message);

        MyMessage read = pack.read(bytes, MyMessage.class);
        System.out.println(read);
    }
}
