function Pit(props) {
  const { pit, playerMove, action } = props;
  const { stones, player, type } = pit;

  const clickable = playerMove === player && type === "HOUSE" && stones > 0;

  const pitClass = "pit "
      + "pit-player-" + (player === "PLAYER_ONE" ? 1 : 2) + " "
      + (type === "STORE" ? "pit-store" : "") + " "
      + (clickable ? "pit-active" : "");

  return (
    <div className={pitClass} onClick={ clickable ? action : undefined }>
      {stones}
    </div>
  );
}

export default Pit;