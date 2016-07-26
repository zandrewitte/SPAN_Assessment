import akka.util.ByteString

/**
  * Package: 
  * Created by zandrewitte on 2016/07/27.
  */
object ScoringFunctions {
  val WIN_SCORE = 3
  val DRAW_SCORE = 1
  val LOSE_SCORE = 0
  val GAME_REGEX = "([\\w\\s]+[\\D])([\\d]+),([\\w\\s]+[\\D])([\\d]+)".r

  def byteStringToGame(bs: ByteString): Game = {
    val GAME_REGEX(team1Name, team1Score, team2Name, team2Score) = bs.utf8String
    Game(TeamResult(team1Name.trim, team1Score.toInt), TeamResult(team2Name.trim, team2Score.toInt))
  }

  def getGameResult(game: Game): List[TeamRank] = game.team1.goals.compareTo(game.team2.goals) match {
    case t if t < 0 => List(TeamRank(game.team1.teamName, LOSE_SCORE), TeamRank(game.team2.teamName, WIN_SCORE))
    case t if t == 0 => List(TeamRank(game.team1.teamName, DRAW_SCORE), TeamRank(game.team2.teamName, DRAW_SCORE))
    case t if t > 0 => List(TeamRank(game.team1.teamName, WIN_SCORE), TeamRank(game.team2.teamName, LOSE_SCORE))
  }

  def groupRanks(teamRanks: List[TeamRank]): List[TeamRank] = teamRanks.groupBy(_.name).map{
    case (teamName, teamScores) => TeamRank(teamName, teamScores.map(_.score).sum)
  }.toList

  def groupScores(teamRanks: List[TeamRank]): List[(Int, List[TeamRank])] = teamRanks.groupBy(_.score).toList
    .sortBy(_._1)(scala.Ordering.Int.reverse)

  def printScoreLog(scoredRanks: List[(Int, List[TeamRank])]): Unit = scoredRanks.foldLeft(1){
    case (ranking, (score, scoreTeamRanks)) =>
      scoreTeamRanks.sortBy(_.name).foreach(teamRank =>
        println(s"${Console.BOLD}$ranking. ${teamRank.name}: ${teamRank.score}${if(teamRank.score == 1)"pt" else "pts"}${Console.RESET}")
      )
      ranking + scoreTeamRanks.size
  }
}

case class Game(team1: TeamResult, team2: TeamResult)
case class TeamResult(teamName: String, goals: Int)
case class TeamRank(name: String, score: Int)
