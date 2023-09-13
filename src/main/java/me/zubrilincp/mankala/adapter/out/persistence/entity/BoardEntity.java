package me.zubrilincp.mankala.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.zubrilincp.mankala.domain.model.Board;
import me.zubrilincp.mankala.domain.model.Pit;
import org.hibernate.annotations.Type;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class BoardEntity {
  @Column(name = "last_used_pit_index")
  private Integer lastUsedPitIndex;

  @Column(name = "pits", nullable = false, columnDefinition = "jsonb")
  @Type(JsonType.class)
  private List<Pit> pits;

  public BoardEntity(Board board) {
    this(board.lastUsedPitIndex(), board.pits());
  }

  public Board toBoard() {
    return new Board(pits, lastUsedPitIndex);
  }
}
