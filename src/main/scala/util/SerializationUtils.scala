package util
import java.io.{
  ByteArrayInputStream,
  ByteArrayOutputStream,
  ObjectInputStream,
  ObjectOutputStream
}

/**
  * Util functions to serialize case class to / from bytes
  */
object SerializationUtils {

  /**
    * Serialize Any to bytes array
    * @param value Any
    * @return byte representation of value
    */
  def serialise(value: Any): Array[Byte] = {
    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(value)
    oos.close()
    stream.toByteArray
  }

  /**
    * Deserialize Array[Byte] into Any
    * @param bytes byte array theoretically representing a serialized object
    * @return deserialized object from byte array
    */
  def deserialize(bytes: Array[Byte]): Any = {
    val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
    val value = ois.readObject
    ois.close()
    value
  }
}
