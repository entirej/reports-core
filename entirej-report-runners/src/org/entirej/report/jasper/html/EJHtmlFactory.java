package org.entirej.report.jasper.html;

import net.sf.jasperreports.engine.util.MarkupProcessor;
import net.sf.jasperreports.engine.util.MarkupProcessorFactory;

/**
	 * 
	 */
	public final class EJHtmlFactory implements MarkupProcessorFactory
	{ 
		@Override
		public MarkupProcessor createMarkupProcessor()
		{
			return EJHtmlMarkupProcessor.getInstance();
		}
	}