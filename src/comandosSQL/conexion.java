package comandosSQL;
import com.mysql.jdbc.Connection;
import static comandosSQL.conexion.con;
import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class conexion {
    
    // Declaramos la conexion a mysql
    public static Connection con;
    // Declaramos los datos de conexion a la bd
    private static final String driver="com.mysql.jdbc.Driver";
    private static final String user="root";
    private static final String pass="12345678";
    private static final String url="jdbc:mysql://localhost:3306/super?characterEncoding=utf8";
    // Funcion que va conectarse a mi bd de mysql
    public Connection conectar(){
      con = null;
      try{
          con = (Connection) DriverManager.getConnection(url, user, pass);
          if(con!=null){
          }
      }
      catch(SQLException e)
      {
          JOptionPane.showMessageDialog(null,"Error" + e.toString());
      }
      return con;
    } 
    
    //Método para llenar comboBox
    public void llenaCombo(String tabla,String valor,JComboBox combo){
        String sql = "select * from " + tabla;
        Statement st;
        conexion con = new conexion();
        Connection conexion = con.conectar();
        try{
            st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                combo.addItem(rs.getString(valor));
            }
        }catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null,"Error" + e.toString());
        } 
    }
    
    public void llenarTablas(JComboBox combo){
        String sql = "show tables";
        Statement st;
        conexion con = new conexion();
        Connection conexion = con.conectar();
        try{
            st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                combo.addItem(rs.getString("Tables_in_veterinaria"));
            }
        }catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null,"Error" + e.toString());
        } 
    }
    
    
    public void RellenaLaTablaConDatosDeMysql(String tabla, JTable visor)
    {
        String sql = "select * from " +tabla;
        Statement st;
        conexion con = new conexion();
        Connection conexion = con.conectar();
        
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("IdProducto");
        model.addColumn("Nombre");
        model.addColumn("Código");
        model.addColumn("Categoría");
        model.addColumn("Precio ($)");
        
        visor.setModel(model);
        
        String [] dato = new String [5];
        try{
            st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                dato[0]=rs.getString(1);
                dato[1]=rs.getString(2);
                dato[2]=rs.getString(3);
                dato[3]=rs.getString(4);
                dato[4]=rs.getString(5);
                model.addRow(dato);
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public void EjecutaElStoredProcedureInsertaProducto(JTextField nombre, JTextField codigo, JTextField categoria, JTextField precio)
   {
       try {            
            conexion conecta = new conexion();
            Connection conexion = conecta.conectar();
            CallableStatement proc = conexion.prepareCall(" CALL SP_INSERTAPRODUCTO(?,?,?,?) ");
            proc.setString(1, nombre.getText());
            proc.setString(2, codigo.getText());
            proc.setString(3, categoria.getText());
            proc.setString(4, precio.getText());
            proc.execute();
        } 
       catch (Exception e) {                  
            System.out.println(e);
       }
   }
   
   public void EliminarRegistro(String id)
   {
       String sql = "delete from productos where idProducto = " + id;
       Statement st;
       Connection conexion = conectar();
       try{
           st = conexion.createStatement();
           int rs = st.executeUpdate(sql);
           JOptionPane.showMessageDialog(null, "Registro eliminado exitosamente");
       }catch(SQLException e)
       {
           System.out.println(e);
       }
   }
}
    
