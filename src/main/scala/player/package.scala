import units.ArmyUnit

package object player {
  type PlayerId = Int

  type Army = Map[ArmyUnit, Int]
}
