package player

case class Player(
    id: PlayerId,
    characteristics: Characteristics,
    money: Int,
    army: Army
)
