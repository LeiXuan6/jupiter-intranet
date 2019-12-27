package org.jupiter.serialization.protobuf;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import org.jupiter.common.util.internal.logging.InternalLogger;
import org.jupiter.common.util.internal.logging.InternalLoggerFactory;
import org.jupiter.serialization.Serializer;
import org.jupiter.serialization.SerializerType;
import org.jupiter.serialization.io.InputBuf;
import org.jupiter.serialization.io.OutputBuf;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Ray
 * @version 2019/12/23-21:18
 **/
public class ProtobufSerializer extends Serializer {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(ProtobufSerializer.class);

    @Override
    public byte code() {
        return SerializerType.PROTOBUF.value();
    }

    @Override
    public <T> OutputBuf writeObject(OutputBuf outputBuf, T obj) {
        throw  new UnsupportedOperationException("不支持这个方法的调用");
    }

    @Override
    public <T> byte[] writeObject(T obj) {
        return ((GeneratedMessageV3)obj).toByteArray();
    }

    @Override
    public <T> T readObject(InputBuf inputBuf, Class<T> clazz) {
        throw  new UnsupportedOperationException("不支持这个方法的调用");
    }

    @Override
    public <T> T readObject(byte[] bytes, int offset, int length, Class<T> messageClazz) {


        try {
            Method method = messageClazz.getDeclaredMethod("parser");
            if (method == null) {
                logger.error("The parser field was not found in {} ", messageClazz);
                return null;
            }
            method.setAccessible(true);
            Object parser = method.invoke(messageClazz);
            if (parser == null) {
                logger.error("The parser obj is null in {}", messageClazz);
                return null;
            }
            if (!(parser instanceof Parser)) {
                logger.error("This parser obj type does not match,parser = {},clazz = {}", parser, messageClazz);
                return null;
            }
            return (T) ((Parser) parser).parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}