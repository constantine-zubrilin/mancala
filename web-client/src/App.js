import './App.css';
import {useState} from 'react';
import Pit from "./components/Pit";

function App() {
  const [partyState, setPartyState] = useState({data: []});
  const [err, setErr] = useState('');

  const newGameHandler = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/v1/party', {
        method: 'POST',
        headers: {
          Accept: 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error(`Error! Status: ${response.status}`);
      }

      const result = await response.json();
      result.board.pits = result.board.pits.map((pit, index) => {
        pit.index = index;
        return pit;
      });
      setPartyState(result);
    } catch (err) {
      setErr(err.message);
    }
  };

  let winner = "It's a draw!";
  let winnerBannerClass = "bg-purple"
  if (partyState.board) {
    const player1Stones = partyState.board.pits.filter(pit => {
      return pit.player === "PLAYER_ONE"
    }).reduce((a, b) => a + b.stones, 0);
    const player2Stones = partyState.board.pits.filter(pit => {
      return pit.player === "PLAYER_TWO"
    }).reduce((a, b) => a + b.stones, 0);
    if (player1Stones > player2Stones) {
      winner = "Player 1 wins!";
      winnerBannerClass = "bg-blue";
    } else if (player2Stones > player1Stones) {
      winner = "Player 2 wins!";
      winnerBannerClass = "bg-red";
    }
  }
  const getMoveHandler = function (pit) {
    return async function () {
      try {
        const response = await fetch(
            'http://localhost:8080/api/v1/party/' + partyState.id + '/move', {
              method: 'POST',
              headers: {
                Accept: 'application/json',
                "Content-Type": "application/json",
              },
              body: JSON.stringify({
                pitIndex: pit.index,
                player: pit.player
              })
            });

        if (!response.ok) {
          throw new Error(`Error! Status: ${response.status}`);
        }

        const result = await response.json();
        result.board.pits = result.board.pits.map((pit, index) => {
          pit.index = index;
          return pit;
        });
        setPartyState(result);
      } catch (err) {
        setErr(err.message);
      }
    }
  };

  return (
      <div className="App">
        <h1 className="cyber-h"><span
            className="cyberpunk-font cyber-glitch-1">Mankala</span> <span
            className="sub-h cyber-glitch-2">3000</span></h1>
        <button className="cyber-button bg-red fg-white"
                onClick={newGameHandler}>
          New game
          <span className="glitchtext">New game</span>
          <span className="tag">R25</span>
        </button>
        <div>
          {err && <div className="ui-state-error">{err}</div>}

          {partyState.id && <div className="party-state">
            <div className="party-state">
              <div className="party-state-row">ID: {partyState.id}</div>
              <div className="party-state-row">State: {partyState.state}</div>
              <div className="party-state-row">Player turn:&nbsp;
                <span className={partyState.playerTurn === "PLAYER_ONE"
                    ? "player-1" : "player-2"}>{partyState.playerTurn}</span>
              </div>
            </div>
            {partyState.state === "FINISHED" && <div
                className={"cyber-banner " + winnerBannerClass}>{winner}</div>}
            <div className="party-state-board">
              <div className="pits pits-player-1">
                {partyState.board.pits.filter(pit => {
                  return pit.player === "PLAYER_ONE"
                })
                .map(pit => {
                  return <Pit pit={pit} playerMove={partyState.playerTurn}
                              action={getMoveHandler(pit)}></Pit>
                })}
              </div>
              <div className="pits pits-player-2">
                {partyState.board.pits.filter(pit => {
                  return pit.player === "PLAYER_TWO"
                })
                .map(pit => {
                  return <Pit pit={pit} playerMove={partyState.playerTurn}
                              action={getMoveHandler(pit)}></Pit>
                })}
              </div>
            </div>
          </div>}
        </div>
      </div>
  );
}

export default App;
