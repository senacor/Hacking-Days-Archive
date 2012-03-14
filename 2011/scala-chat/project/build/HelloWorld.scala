import sbt._

class HelloWorldProject(info: ProjectInfo) extends DefaultWebProject(info)
{
  lazy val hi = task { println("Hello World"); None }

  val mavenLocal = "Local Maven Repository" at "file:///"+Path.userHome+"/.m2/repository"

}