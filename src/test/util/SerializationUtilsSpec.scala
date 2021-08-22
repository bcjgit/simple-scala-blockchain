package util

import org.scalatest.flatspec.AnyFlatSpec

class SerializationUtilsSpec extends AnyFlatSpec {

  "A string of > 0 length" should "serialize to an array of > 0 length" in {
    assert(SerializationUtils.serialise("Hello World!").length > 0)
  }
}
