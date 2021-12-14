package units

sealed trait ArmyUnit

object ArmyUnit {
  case object LightUnit extends ArmyUnit
  case object MediumUnit extends ArmyUnit
  case object HeavyUnit extends ArmyUnit

  object strengths {
    val lightUnit: Int = 1
    val mediumUnit: Int = 3
    val heavyUnit: Int = 5
  }
}
