package org.entirej.report.poi.layout;

import java.util.ArrayList;
import java.util.List;

import org.entirej.framework.report.EJReportBlock;

public class EJReportPOIBlock implements IBlockParent
{
    private final EJReportBlock block;
    private List<EJReportPOIRaw>   raws   = new ArrayList<EJReportPOIRaw>();

    private List<EJReportPOIBlock> blocks = new ArrayList<EJReportPOIBlock>();

    public EJReportPOIBlock(EJReportBlock block)
    {
        this.block = block;
    }

    public void addRaw(EJReportPOIRaw raw)
    {
        raws.add(raw);
    }

    public List<EJReportPOIBlock> getBlocks()
    {
        return new ArrayList<EJReportPOIBlock>(blocks);
    }

    public void addBlock(EJReportPOIBlock block)
    {
        blocks.add(block);

    }

    public List<EJReportPOIRaw> getRaws()
    {
        return new ArrayList<EJReportPOIRaw>(raws);
    }

    public EJReportBlock getBlock()
    {
        return block;
    }

}
