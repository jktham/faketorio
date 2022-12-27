package faketorio.entities.buildings;

import org.joml.Vector2i;
import org.joml.Vector3f;

import faketorio.engine.App;
import faketorio.entities.Entity;

public class Building extends Entity {
	public Vector2i tilePos;
	public int rotation;

	public boolean ghost;

	public Building output;
	
	public Building(Vector2i tilePos, int rotation) {
		super(new Vector3f(tilePos.x, tilePos.y, 0f));
		this.tilePos = new Vector2i(tilePos);
		this.rotation = rotation;
		ghost = false;
		output = App.world.getBuilding(getOutputTilePos());
	}

	public void update() {
		output = App.world.getBuilding(getOutputTilePos());
		updateBorders();
	}
	
	public Vector2i getOutputTilePos() {
		Vector2i outputTilePos = null;
		if (rotation == 0) {
			outputTilePos = new Vector2i(tilePos).add(0, -1);
		} else if (rotation == 1) {
			outputTilePos = new Vector2i(tilePos).add(1, 0);
		} else if (rotation == 2) {
			outputTilePos = new Vector2i(tilePos).add(0, 1);
		} else if (rotation == 3) {
			outputTilePos = new Vector2i(tilePos).add(-1, 0);
		}
		return outputTilePos;
	}

	public void updateBorders() {
		model.meshHidden.set(1, true);
		model.meshHidden.set(2, true);
		model.meshHidden.set(3, true);
		model.meshHidden.set(4, true);

		if (output != null && output.canBeAdded(-1, 0, this)) {
			model.meshHidden.set(4, false);
		}
		if (App.world.ghost != null && getOutputTilePos().equals(App.world.ghost.tilePos) && App.world.ghost.canBeAdded(-1, 0, this)) {
			model.meshHidden.set(4, false);
		}

		if (App.world.getBuilding(new Vector2i(tilePos).add(0, 1)) != null && App.world.getBuilding(new Vector2i(tilePos).add(0, 1)).getOutputTilePos().equals(tilePos)) {
			model.meshHidden.set(getBorderIndex(0), false);
		}
		if (App.world.getBuilding(new Vector2i(tilePos).add(1, 0)) != null && App.world.getBuilding(new Vector2i(tilePos).add(1, 0)).getOutputTilePos().equals(tilePos)) {
			model.meshHidden.set(getBorderIndex(1), false);
		}
		if (App.world.getBuilding(new Vector2i(tilePos).add(0, -1)) != null && App.world.getBuilding(new Vector2i(tilePos).add(0, -1)).getOutputTilePos().equals(tilePos)) {
			model.meshHidden.set(getBorderIndex(2), false);
		}
		if (App.world.getBuilding(new Vector2i(tilePos).add(-1, 0)) != null && App.world.getBuilding(new Vector2i(tilePos).add(-1, 0)).getOutputTilePos().equals(tilePos)) {
			model.meshHidden.set(getBorderIndex(3), false);
		}
		if (App.world.ghost != null && App.world.ghost.tilePos.equals(new Vector2i(tilePos).add(0, 1)) && App.world.ghost.getOutputTilePos().equals(tilePos)) {
			model.meshHidden.set(getBorderIndex(0), false);
		}
		if (App.world.ghost != null && App.world.ghost.tilePos.equals(new Vector2i(tilePos).add(1, 0)) && App.world.ghost.getOutputTilePos().equals(tilePos)) {
			model.meshHidden.set(getBorderIndex(1), false);
		}
		if (App.world.ghost != null && App.world.ghost.tilePos.equals(new Vector2i(tilePos).add(0, -1)) && App.world.ghost.getOutputTilePos().equals(tilePos)) {
			model.meshHidden.set(getBorderIndex(2), false);
		}
		if (App.world.ghost != null && App.world.ghost.tilePos.equals(new Vector2i(tilePos).add(-1, 0)) && App.world.ghost.getOutputTilePos().equals(tilePos)) {
			model.meshHidden.set(getBorderIndex(3), false);
		}
	}

	public int getBorderIndex(int dir) {
		if (dir == 0) {
			if (rotation == 0) {
				return 2;
			} else if (rotation == 1) {
				return 3;
			} else if (rotation == 2) {
				return 4;
			} else if (rotation == 3) {
				return 1;
			}
		} else if (dir == 1) {
			if (rotation == 0) {
				return 3;
			} else if (rotation == 1) {
				return 4;
			} else if (rotation == 2) {
				return 1;
			} else if (rotation == 3) {
				return 2;
			}
		} else if (dir == 2) {
			if (rotation == 0) {
				return 4;
			} else if (rotation == 1) {
				return 1;
			} else if (rotation == 2) {
				return 2;
			} else if (rotation == 3) {
				return 3;
			}
		} else if (dir == 3) {
			if (rotation == 0) {
				return 1;
			} else if (rotation == 1) {
				return 2;
			} else if (rotation == 2) {
				return 3;
			} else if (rotation == 3) {
				return 4;
			}
		}
		return 0;
	}
}
