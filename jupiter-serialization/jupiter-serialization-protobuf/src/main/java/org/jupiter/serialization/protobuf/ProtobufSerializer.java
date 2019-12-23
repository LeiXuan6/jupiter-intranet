package org.jupiter.serialization.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import org.jupiter.common.util.internal.logging.InternalLogger;
import org.jupiter.common.util.internal.logging.InternalLoggerFactory;
import org.jupiter.serialization.Serializer;
import org.jupiter.serialization.SerializerType;
import org.jupiter.serialization.io.InputBuf;
import org.jupiter.serialization.io.OutputBuf;

import java.lang.reflect.Field;

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
        return null;
    }

    @Override
    public <T> byte[] writeObject(T obj) {
        return new byte[0];
    }

    @Override
    public <T> T readObject(InputBuf inputBuf, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T readObject(byte[] bytes, int offset, int length, Class<T> messageClazz) {
        try {
            Field field = messageClazz.getDeclaredField("PARSER");
            if (field == null) {
                logger.error("The parser field was not found in {} ", messageClazz);
                return null;
            }
            field.setAccessible(true);
            Object parser = field.get(messageClazz);
            if (parser == null) {
                logger.error("The parser obj is null in {}", messageClazz);
                return null;
            }
            if (!(parser instanceof Parser)) {
                logger.error("This parser obj type does not match,parser = {},clazz = {}", parser, messageClazz);
                return null;
            }
            return (T) ((Parser) parser).parseFrom(bytes);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}