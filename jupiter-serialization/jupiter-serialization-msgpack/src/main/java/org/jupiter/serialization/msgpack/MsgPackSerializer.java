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
package org.jupiter.serialization.msgpack;

import org.jupiter.serialization.Serializer;
import org.jupiter.serialization.SerializerType;
import org.jupiter.serialization.io.InputBuf;
import org.jupiter.serialization.io.OutputBuf;
import org.msgpack.MessagePack;

import java.io.IOException;

/**
 * @author JackLei
 * @version 2020-03-29
 */
public class MsgPackSerializer extends Serializer {
    @Override
    public byte code() {
        return SerializerType.MSGPACK.value();
    }

    @Override
    public <T> OutputBuf writeObject(OutputBuf outputBuf, T obj) {
        throw  new UnsupportedOperationException("不支持这个方法的调用");
    }

    @Override
    public <T> byte[] writeObject(T obj) {
        try {
            MessagePack messagePack = new MessagePack();
            return messagePack.write(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T readObject(InputBuf inputBuf, Class<T> clazz) {
        throw  new UnsupportedOperationException("不支持这个方法的调用");
    }

    @Override
    public <T> T readObject(byte[] bytes, int offset, int length, Class<T> clazz) {
        MessagePack messagePack = new MessagePack();
        try {
            return messagePack.read(bytes,clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
