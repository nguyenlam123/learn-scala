error id: file://<WORKSPACE>/core/src/main/scala/example/core/Weather.scala:`<none>`.
file://<WORKSPACE>/core/src/main/scala/example/core/Weather.scala
empty definition using pc, found symbol in pc: `<none>`.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -sttp/client4/quick/String#
	 -String#
	 -scala/Predef.String#
offset: 142
uri: file://<WORKSPACE>/core/src/main/scala/example/core/Weather.scala
text:
```scala
package example.core

import sttp.client4.quick._
import sttp.client4.Response

object Weather {
  def temp() = {
    val response: Response[S@@tring] = quickRequest
      .get(
        uri"https://api.open-meteo.com/v1/forecast?latitude=40.7143&longitude=-74.006&current_weather=true"
      )
      .send()
    val json = ujson.read(response.body)
    json.obj("current_weather")("temperature").num
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.