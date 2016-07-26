
import org.scalatest._
import ScoringFunctions._
/**
  * Package: 
  * Created by zandrewitte on 2016/07/27.
  */
class ScoringSpec extends FlatSpec{


  "A team" should "get 3 points when winning a game" in {
    val result = getGameResult(Game(TeamResult("Lions", 2), TeamResult("Snakes", 1)))
    assert(result.head.score == 3)
  }

  "A team" should "get 0 points when losing a game" in {
    val result = getGameResult(Game(TeamResult("Lions", 0), TeamResult("Snakes", 1)))
    assert(result.head.score == 0)
  }

  "Both teams" should "get 1 point when drawing" in {
    val result = getGameResult(Game(TeamResult("Lions", 0), TeamResult("Snakes", 0)))
    assert(result.forall(_.score == 1))
  }

  "A team" should "get all their points from all the games grouped" in {
    val result = groupRanks(List(TeamRank("Lions", 3), TeamRank("Lions", 1), TeamRank("Lions", 0), TeamRank("Lions", 3)))
    assert(result.head.score == 7)
  }

  "Teams with same rank" should "be sorted alphabetically" in {
    val result = groupScores(List(TeamRank("Snakes", 5), TeamRank("Lions", 5)))
    assert(result.flatMap(_._2.sortBy(_.name)).head.name == "Lions")
  }

}