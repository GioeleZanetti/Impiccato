/**Interfaccia per JPanel
 *
 * @author Gioele Zanetti
 * @version 11.11.2021
 */

package graphic;

import javax.swing.JPanel;

public interface Addable {
    
    /**
     * Metodo per impostare dei parametri all'interno di un JPanel
     * @param o il valore
     * @param parameter il nome di cosa impostare
     */
    public void setData(Object o, String parameter);
    
}
