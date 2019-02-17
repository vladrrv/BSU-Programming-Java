import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

class DateTableListener implements TableModelListener {
    private DateTableModel model;

    public DateTableListener(DateTableModel model) {
        this.model = model;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        model.updateSelf(e.getLastRow(), e.getColumn());
    }
}
