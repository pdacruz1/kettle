 /**********************************************************************
 **                                                                   **
 **               This code belongs to the KETTLE project.            **
 **                                                                   **
 ** Kettle, from version 2.2 on, is released into the public domain   **
 ** under the Lesser GNU Public License (LGPL).                       **
 **                                                                   **
 ** For more details, please read the document LICENSE.txt, included  **
 ** in this project                                                   **
 **                                                                   **
 ** http://www.kettle.be                                              **
 ** info@kettle.be                                                    **
 **                                                                   **
 **********************************************************************/
 
package org.pentaho.di.trans.steps.blockuntilstepsfinish;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.TransMeta.TransformationType;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

/*
 * Created on 30-06-2008
 *
 */


public class BlockUntilStepsFinishMeta extends BaseStepMeta implements StepMetaInterface
{
	private static Class<?> PKG = BlockUntilStepsFinishMeta.class; // for i18n purposes, needed by Translator2!!   $NON-NLS-1$

    /** by which steps to display? */
    private String  stepName[];
    private String stepCopyNr[];
    
	public BlockUntilStepsFinishMeta()
	{
		super(); // allocate BaseStepMeta
	}
	
   public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
	throws KettleXMLException
	{
    	readData(stepnode);
	}

	public Object clone()
	{	
        BlockUntilStepsFinishMeta retval = (BlockUntilStepsFinishMeta) super.clone();

        int nrfields = stepName.length;

        retval.allocate(nrfields);

        for (int i = 0; i < nrfields; i++)
        {
            retval.stepName[i] = stepName[i];
            retval.stepCopyNr[i] = stepCopyNr[i];
            
        }
		return retval;
	}
	   public void allocate(int nrfields)
	    {
	        stepName = new String[nrfields]; 
	        stepCopyNr= new String[nrfields]; 
	    }
    /**
     * @return Returns the stepName.
     */
    public String[] getStepName()
    {
        return stepName;
    }
    
    /**
     * @return Returns the stepCopyNr.
     */
    public String[] getStepCopyNr()
    {
        return stepCopyNr;
    }
    
    /**
     * @param stepName The stepName to set.
     */
    public void setStepName(String[] stepName)
    {
        this.stepName = stepName;
    }
    /**
     * @param stepCopyNr The stepCopyNr to set.
     */
    public void setStepCopyNr(String[] stepCopyNr)
    {
        this.stepCopyNr = stepCopyNr;
    }
    
    
    public void getFields(RowMetaInterface r, String name, RowMetaInterface info[], StepMeta nextStep, VariableSpace space) throws KettleStepException
	{

    }
	private void readData(Node stepnode)  throws KettleXMLException
	{
	  try
	    {
		  Node steps = XMLHandler.getSubNode(stepnode, "steps");
          int nrsteps = XMLHandler.countNodes(steps, "step");

          allocate(nrsteps);

          for (int i = 0; i < nrsteps; i++)
          {
              Node fnode = XMLHandler.getSubNodeByNr(steps, "step", i);
              stepName[i] = XMLHandler.getTagValue(fnode, "name");
              stepCopyNr[i] = XMLHandler.getTagValue(fnode, "CopyNr");
          }
	    }
      catch (Exception e)
      {
          throw new KettleXMLException("Unable to load step info from XML", e);
      }
	}
   public String getXML()
    {
        StringBuffer retval = new StringBuffer();
        
        retval.append("    <steps>" + Const.CR);
        for (int i = 0; i < stepName.length; i++)
        {
            retval.append("      <step>" + Const.CR);
            retval.append("        " + XMLHandler.addTagValue("name", stepName[i]));
            retval.append("        " + XMLHandler.addTagValue("CopyNr", stepCopyNr[i]));
            
            retval.append("        </step>" + Const.CR);
        }
        retval.append("      </steps>" + Const.CR);

        return retval.toString();
    }
	public void setDefault()
	{
        int nrsteps = 0;

        allocate(nrsteps);

        for (int i = 0; i < nrsteps; i++)
        {
            stepName[i] = "step" + i;
            stepCopyNr[i] = "CopyNr" + i;
        }
	}


	public void readRep(Repository rep, ObjectId id_step, List<DatabaseMeta> databases, Map<String, Counter> counters)
	throws KettleException
	{
	        try
	        {

	            int nrsteps = rep.countNrStepAttributes(id_step, "step_name");

	            allocate(nrsteps);

	            for (int i = 0; i < nrsteps; i++)
	            {
	                stepName[i] = rep.getStepAttributeString(id_step, i, "step_name");
	                stepCopyNr[i] = rep.getStepAttributeString(id_step, i, "step_CopyNr");
	            }
	        }
	        catch (Exception e)
	        {
	            throw new KettleException("Unexpected error reading step information from the repository", e);
	        }
	    }
	
		public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step)
		throws KettleException
		{
			try  {
	            for (int i = 0; i < stepName.length; i++)
	            {
	                rep.saveStepAttribute(id_transformation, id_step, i, "step_name", stepName[i]);
	                rep.saveStepAttribute(id_transformation, id_step, i, "step_CopyNr", stepCopyNr[i]);
	                
	            }
	        }
	        catch (Exception e)
	        {
	            throw new KettleException("Unable to save step information to the repository for id_step=" + id_step, e);
	        }
	    }
		
	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		CheckResult cr;
		
		if (prev==null || prev.size()==0)
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_WARNING, BaseMessages.getString(PKG, "BlockUntilStepsFinishMeta.CheckResult.NotReceivingFields"), stepMeta); //$NON-NLS-1$
		}
		else
		{
            if (stepName.length > 0)
                cr = new CheckResult(CheckResult.TYPE_RESULT_OK, BaseMessages.getString(PKG, "BlockUntilStepsFinishMeta.CheckResult.AllStepsFound"), stepMeta);
            else
                cr = new CheckResult(CheckResult.TYPE_RESULT_WARNING, BaseMessages.getString(PKG, "BlockUntilStepsFinishMeta.CheckResult.NoStepsEntered"), stepMeta);
                
		}
		remarks.add(cr);
		
		// See if we have input streams leading to this step!
		if (input.length>0)
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, BaseMessages.getString(PKG, "BlockUntilStepsFinishMeta.CheckResult.StepRecevingData2"), stepMeta); //$NON-NLS-1$
		else
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, BaseMessages.getString(PKG, "BlockUntilStepsFinishMeta.CheckResult.NoInputReceivedFromOtherSteps"), stepMeta); //$NON-NLS-1$
		remarks.add(cr);
		
	}
	

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta tr, Trans trans)
	{
		return new BlockUntilStepsFinish(stepMeta, stepDataInterface, cnr, tr, trans);
	}
	
	public StepDataInterface getStepData()
	{
		return new BlockUntilStepsFinishData();
	}

    public TransformationType[] getSupportedTransformationTypes() {
      return new TransformationType[] { TransformationType.Normal, };
    }
}
