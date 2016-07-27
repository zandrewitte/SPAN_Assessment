import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, IOResult}
import akka.util.ByteString
import scalaz._, Scalaz._
import akka.stream.scaladsl.{Sink => StreamSink, _}
import ScoringFunctions._
/**
  * Package: 
  * Created by zandrewitte on 2016/07/25.
  */
object Main extends App{

  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()

  println(s"${Console.GREEN}Please enter file names to process: ${Console.RESET}")
  io.Source.stdin.getLines().foreach { fileName =>
    println(s"${Console.BLUE}Processing File: $fileName${Console.RESET}")
    val file = Paths.get(fileName)

    FileIO.fromPath(file)
      .via(Framing.delimiter(ByteString(System.lineSeparator), maximumFrameLength = 512, allowTruncation = true))
      .map{_ |> byteStringToGame |> getGameResult }
      .fold(Map[String, Int]())(_ |+| _)
      .runWith(StreamSink.foreach{_ |> groupScores |> printScoreLog })
  }

}