package admin;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Admin implements Initializable {
//    private final String machinefile = "application/Name_machine.txt";
    private  String saverPath = "C:\\Users\\Tuan Nguyen\\IdeaProjects\\Final_Project\\src\\admin\\saver";
    private  String activityFile = "admin/activities";
    private  ArrayList<Activity> activity_List= new ArrayList<Activity>();
    private  HashMap<String, Double> activitiesList = new HashMap<String, Double>();
    private  HashMap<String, Double> hash_bar = new HashMap<String, Double>();
//    private final String temp_list_path = "temp_list.dat";
    private final HashMap<String, Double> machineList = new HashMap<String, Double>();
    private  HashMap<Date, HashMap<String, Double>> list = new HashMap<Date, HashMap<String, Double>>();
    private final FileChooser filechoose = new FileChooser();
    public void PrintWhatInSaver() {
        System.out.println("This is in Saver: "+GetCurrentFile().getPath());

    }

    @FXML
    private ComboBox<String> activitiesCmb;
    @FXML
    private TextField hours;
    @FXML
    private DatePicker date;
    @FXML
    private TextArea textArea;

    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis chartX;

    @FXML
    private NumberAxis chartY;

    public void readActivities() throws IOException{

        ClassLoader cl = this.getClass().getClassLoader();
        URL url = cl.getResource(activityFile);


        try(InputStream in = url.openStream(); BufferedReader input = new BufferedReader(new InputStreamReader(in))){

            String temp;
            int i = 0;
            while((temp = input.readLine()) != null) {
                String[] parts = temp.split(" ");
                activitiesList.put(parts[0], Double.parseDouble(parts[1]));

            }
            List<String> names = activitiesList.keySet().stream().sorted().collect(Collectors.toList());
            ObservableList<String> options = FXCollections.observableArrayList(names);

            activitiesCmb.setItems(FXCollections.observableArrayList(names));



        }
    }


    public double sum_(String date , ArrayList<Activity> ac){
        double sum = 0;
        for (Activity a: ac
             ) {
            if (a.getDate().equals(date)){
                sum += a.getCal();
            }
        }
        return sum;
    }

    @FXML
    void AddActivity(ActionEvent event) {

        double hours= Double.parseDouble(this.hours.getText());
        LocalDate date = this.date.getValue();
        Date date1 = java.sql.Date.valueOf(date);
        String nameofActitvity = activitiesCmb.getValue();
        String date_format = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        System.out.println(hours);
        System.out.println(date_format);
        System.out.println(nameofActitvity);

//        try {
//            activity_List = readSaver();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        activity_List.add(new Activity(date_format, nameofActitvity, activitiesList.get(nameofActitvity) * hours));
//        WriteSaver(activity_List);

        String text ="";
        for (Activity a: activity_List
             ) {
            text += a.toString() + "\n";
            double s = sum_(a.getDate(), activity_List);
            hash_bar.put(a.getDate(), s);

        }
        System.out.println(hash_bar.keySet());
        textArea.setText(text);
        chartX.setLabel("date");
        chartY.setLabel("Cal");
        XYChart.Series<String, Number> set1 = new XYChart.Series<>();
        chartX.setAnimated(false);

        barChart.getData().clear();

        hash_bar.entrySet().forEach(entry->{

            set1.getData().add(new XYChart.Data<String, Number>(entry.getKey(), entry.getValue()));
        });

        barChart.getData().addAll(set1);

    }

    @FXML
    private Button saveFile;
    @FXML
    void SavetoFile(ActionEvent event) {
        Stage stage = new Stage();
        File file = filechoose.showSaveDialog(stage);
        if (file != null){
            try {

                FileOutputStream fileOut = new FileOutputStream(file.getPath());
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                for (Activity a: activity_List
                     ) {

                    System.out.println(a.toString());
                }
                objectOut.writeObject(activity_List);

                System.out.println("The Object  was succesfully written to a file");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    @FXML
    void OpenFile(ActionEvent event) {
        Stage stage = new Stage();
        File file = filechoose.showOpenDialog(stage);
        HashMap<String, Double> hash_bar = new HashMap<String, Double>();
        if (file != null) {
            try {

                try (ObjectInputStream file_in
                             = new ObjectInputStream(new FileInputStream(file.getPath()))){
                    this.activity_List = (ArrayList<Activity>) file_in.readObject();
                    System.out.println("read ok");

                }
                catch(Exception e) {
                    System.out.println("No File chosen  " + file.getPath());
		            e.printStackTrace();

                }


                String text ="";
                for (Activity a: activity_List
                        ) {
                    text += a.toString() + "\n";
                    double s = sum_(a.getDate(), activity_List);
                    hash_bar.put(a.getDate(), s);

                }
                System.out.println(hash_bar.keySet());
                textArea.setText(text);
                chartX.setLabel("date");
                chartY.setLabel("Cal");
                XYChart.Series<String, Number> set1 = new XYChart.Series<>();
                chartX.setAnimated(false);

                barChart.getData().clear();

                hash_bar.entrySet().forEach(entry->{

                    set1.getData().add(new XYChart.Data<String, Number>(entry.getKey(), entry.getValue()));
                });
                barChart.getData().addAll(set1);
            } catch (Exception ex) {
                ex.printStackTrace();

            }

        }
    }


    @FXML
    private Button btn_exit;
    @FXML
    private Button txt_open;

    @FXML
    private Button txt_save;

    @FXML
    private TextField adminTFSearch;

    @FXML
    private TableView<?> adminTableView;

    @FXML
    private TableColumn<?, ?> adminTableNo;

    @FXML
    private TableColumn<?, ?> adminTableDate;

    @FXML
    private TableColumn<?, ?> adminTableActivities;

    @FXML
    private TableColumn<?, ?> adminTableHours;

    @FXML
    void setAdminCloseButtonClick(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) btn_exit.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    public void WriteFileToSaver(File file) {
        System.out.println("=======Now Start Write Current File in Saver=============");
        try (ObjectOutputStream file_out
                     = new ObjectOutputStream(new FileOutputStream(saverPath))){
            file_out.writeObject(file);
        }
        catch(Exception e) {
            System.out.println("Problems with save file to saver.dat " + saverPath);
//	            e.printStackTrace();
        }
        System.out.println("=======Now End Write Current File in Saver=============");
    }

    @FXML
    void setAdminOpenButtonClick(ActionEvent event) {
        System.out.println("=======Now Start Open File=============");
        Stage stage = new Stage();
        File file = filechoose.showOpenDialog(stage);
        if (file != null) {

            WriteFileToSaver(file);

        }
        System.out.println("This is Open File: " );
        PrintWhatInSaver();
        System.out.println("=======Now End Open File=============");

    }

    @FXML
    void setAdminDeleteButtonClick(ActionEvent event) {

    }

    @FXML
    void setAdminEditButtonClick(ActionEvent event) {

    }

    @FXML
    void setAdminSearchButtonClick(ActionEvent event) {

    }
    public File GetCurrentFile() {
        System.out.println("=======Now Start read Current File in Saver=============");
        try (ObjectInputStream file_in
                     = new ObjectInputStream(new FileInputStream(saverPath))){
            File file = (File)file_in.readObject();
            System.out.println("=======Now End read Current File in Saver=============");
            return file;
        }
        catch(Exception e) {
            System.out.println("No chosen File " );


        }
        System.out.println("=======Now End read Current File in Saver=============");
        return null;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            readActivities();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private Button about;
    @FXML
    void Aboutfunction(ActionEvent event) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("about.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("Activities Management");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
