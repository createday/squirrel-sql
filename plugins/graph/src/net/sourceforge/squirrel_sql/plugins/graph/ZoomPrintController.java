package net.sourceforge.squirrel_sql.plugins.graph;

import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.plugins.graph.xmlbeans.FormatXmlBean;
import net.sourceforge.squirrel_sql.plugins.graph.xmlbeans.PrintXmlBean;
import net.sourceforge.squirrel_sql.plugins.graph.xmlbeans.ZoomerXmlBean;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.print.PrinterJob;


public class ZoomPrintController
{
   ZoomPrintPanel _panel = new ZoomPrintPanel();

   Zoomer _zoomer;
   private boolean _dontReactToSliderChanges = false;
   private ISession _session;
   private FormatController _formatController;
   private EdgesListener _edgesListener;
   private EdgesGraphComponent _edgesGraphComponent;
   private JComponent _toPrint;
   private PageCountCallBack _pageCountCallBack;
   private GraphPlugin _plugin;

   public ZoomPrintController(ZoomerXmlBean zoomerXmlBean, PrintXmlBean printXmlBean, EdgesListener edgesListener, JComponent toPrint, ISession session, GraphPlugin plugin, PageCountCallBack pageCountCallBack)
   {
      _toPrint = toPrint;
      _pageCountCallBack = pageCountCallBack;
      _plugin = plugin;

      initZoom(session, zoomerXmlBean);
      initPrint(printXmlBean, edgesListener);
   }

   private void initPrint(PrintXmlBean printXmlBean, EdgesListener edgesListener)
   {
      _edgesListener = edgesListener;

      FormatControllerListener fcl = new FormatControllerListener()
      {
         public void formatsChanged(FormatXmlBean selectedFormat)
         {
            onFormatsChanged(selectedFormat);
         }
      };

      if(null != printXmlBean)
      {
         _formatController = new FormatController(_session, _plugin, fcl);
         _panel.sldEdges.setValue(printXmlBean.getEdgesScale());
         _panel.chkShowEdges.setSelected(printXmlBean.isShowEdges());
      }
      else
      {
         _formatController = new FormatController(_session, _plugin, fcl);
      }

      FormatXmlBean[] formats =_formatController.getFormats();

      FormatXmlBean toSelect = null;
      for (int i = 0; i < formats.length; i++)
      {
         _panel.cboFormat.addItem(formats[i]);
         if(formats[i].isSelected())
         {
            toSelect = formats[i];
         }
      }
      if(null != toSelect)
      {
         _panel.cboFormat.setSelectedItem(toSelect);
      }

      _panel.sldEdges.addChangeListener(new ChangeListener()
      {
         public void stateChanged(ChangeEvent e)
         {
            onSldEdgesChanged();
         }
      });


      _panel.chkShowEdges.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            onShowEdges();
         }
      });

      onShowEdges();

      _panel.btnPrint.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            onPrint();
         }
      });

      _panel.cboFormat.addItemListener(new ItemListener()
      {
         public void itemStateChanged(ItemEvent e)
         {
            onSelectedFormatChanged(e);
         }
      });

   }

   private void onShowEdges()
   {
      _panel.btnFormat.setEnabled(_panel.chkShowEdges.isSelected());
      _panel.cboFormat.setEnabled(_panel.chkShowEdges.isSelected());
      _panel.sldEdges.setEnabled(_panel.chkShowEdges.isSelected());
      _panel.btnPrint.setEnabled(_panel.chkShowEdges.isSelected());

      fireEdgesGraphComponentChanged(_panel.chkShowEdges.isSelected());
   }

   private void onSelectedFormatChanged(ItemEvent e)
   {
      if(ItemEvent.SELECTED == e.getStateChange())
      {
         fireEdgesGraphComponentChanged(_panel.chkShowEdges.isSelected());
      }
   }

   private void onFormatsChanged(FormatXmlBean selectedFormat)
   {
      FormatXmlBean[] formats = _formatController.getFormats();
      _panel.cboFormat.removeAllItems();
      for (int i = 0; i < formats.length; i++)
      {
         _panel.cboFormat.addItem(formats[i]);
         if(formats[i] == selectedFormat)
         {
            formats[i].setSelected(true);
            _panel.cboFormat.setSelectedItem(formats[i]);
         }
         else
         {
            formats[i].setSelected(false);
         }
      }
   }

   private void onPrint()
   {
      GraphPrintable graphPrintable = new GraphPrintable((FormatXmlBean)_panel.cboFormat.getSelectedItem(), _toPrint, _panel.sldEdges.getValue() / 100.0, _pageCountCallBack);

      PrinterJob printJob = PrinterJob.getPrinterJob();
      printJob.setPrintable(graphPrintable);
      if (printJob.printDialog())
      {
         try
         {
            printJob.print();
         }
         catch (Exception ex)
         {
            throw new RuntimeException(ex);
         }
      }
   }

   private void fireEdgesGraphComponentChanged(boolean showEdges)
   {
      if(null == _edgesGraphComponent)
      {
         _edgesGraphComponent = new EdgesGraphComponent();
      }

      if(showEdges)
      {
         FormatXmlBean format = (FormatXmlBean) _panel.cboFormat.getSelectedItem();
         _edgesGraphComponent.init(format, _panel.sldEdges.getValue() / 100.0, _panel.sldEdges.getValueIsAdjusting());
         _edgesListener.edgesGraphComponentChanged(_edgesGraphComponent, true);

      }
      else
      {
         _edgesListener.edgesGraphComponentChanged(_edgesGraphComponent, false);
      }

   }

   private void onSldEdgesChanged()
   {
      fireEdgesGraphComponentChanged(_panel.chkShowEdges.isSelected());
   }

   private void initZoom(ISession session, ZoomerXmlBean zoomerXmlBean)
   {
      _session = session;
      _zoomer = new Zoomer(zoomerXmlBean);

      _panel.setVisible(false);

      _panel.sldZoom.addChangeListener(new ChangeListener()
      {
         public void stateChanged(ChangeEvent e)
         {
            onSldZoomChanged();
         }
      });

      _panel.chkHideScrollBars.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            onHideScrollbars();
         }
      });

      _panel.btnFormat.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            onBtnFormat();
         }
      });
   }

   private void onBtnFormat()
   {
      _formatController.setVisible(true);
   }

   private void onHideScrollbars()
   {
      _zoomer.setHideScrollBars(_panel.chkHideScrollBars.isSelected());
   }

   private void onSldZoomChanged()
   {
      if(_dontReactToSliderChanges)
      {
         return;
      }
      _zoomer.setZoom(_panel.sldZoom.getValue() / 100.0, _panel.sldZoom.getValueIsAdjusting());
   }

   public ZoomPrintPanel getPanel()
   {
      return _panel;
   }

   public void setVisible(boolean b)
   {
      _panel.setVisible(b);
      _zoomer.setEnabled(b);

      onShowEdges();

      try
      {
         _dontReactToSliderChanges = true;
         _panel.sldZoom.setValue((int)(_zoomer.getZoom() * 100 + 0.5));
      }
      finally
      {
         _dontReactToSliderChanges = false;
      }
   }

   public Zoomer getZoomer()
   {
      return _zoomer;
   }

   public PrintXmlBean getPrintXmlBean()
   {
      PrintXmlBean ret = new PrintXmlBean();
      ret.setShowEdges(_panel.chkShowEdges.isSelected());
      ret.setEdgesScale(_panel.sldEdges.getValue());

      return ret;
   }

   public void sessionEnding()
   {
      _formatController.close();
   }
}
