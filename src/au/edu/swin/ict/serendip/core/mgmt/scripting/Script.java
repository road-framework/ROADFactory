package au.edu.swin.ict.serendip.core.mgmt.scripting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Script {

    private List<Block> blocks = new ArrayList<Block>();

    public void addBlock(Block block) {
	this.blocks.add(block);
    }

    public List<Block> getBlocks() {
	return this.blocks;
    }

    public String toString() {
	StringBuffer buffer = new StringBuffer();

	Iterator<Block> blockIter = blocks.iterator();
	while (blockIter.hasNext()) {
	    Block block = (Block) blockIter.next();
	    buffer.append(block);
	}

	return buffer.toString();
    }
}
