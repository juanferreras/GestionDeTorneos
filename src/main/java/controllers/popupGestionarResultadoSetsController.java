package controllers;

import controllers.general.ControlledScreen;
import dtos.ResultadoSetDTO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Partido;
import models.Resultado;
import services.GestorCompetencia;

import java.util.ArrayList;
import java.util.List;

public class popupGestionarResultadoSetsController extends ControlledScreen {

    private int idCompetencia;
    private int idPartidoClickeado;
    private int cantSets;
    private Partido partido;
    private GestorCompetencia gestorCompetencia = new GestorCompetencia();
    private ChangeListener<Integer> listenerSpinner = new ChangeListener<Integer>() {
        @Override
        public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
            spinnerChange();
        }
    };

    @FXML private Button okButton;
    @FXML private Button cancelarButton;
    @FXML private Label detailsLabel;
    @FXML private Label error1;
    @FXML private CheckBox sePresentoLocalCheckBox;
    @FXML private CheckBox sePresentoVisitanteCheckBox;
    @FXML private Spinner localSpinner1;
    @FXML private Spinner localSpinner2;
    @FXML private Spinner localSpinner3;
    @FXML private Spinner localSpinner4;
    @FXML private Spinner localSpinner5;
    @FXML private Spinner localSpinner6;
    @FXML private Spinner localSpinner7;
    @FXML private Spinner localSpinner8;
    @FXML private Spinner localSpinner9;
    @FXML private Spinner visitanteSpinner1;
    @FXML private Spinner visitanteSpinner2;
    @FXML private Spinner visitanteSpinner3;
    @FXML private Spinner visitanteSpinner4;
    @FXML private Spinner visitanteSpinner5;
    @FXML private Spinner visitanteSpinner6;
    @FXML private Spinner visitanteSpinner7;
    @FXML private Spinner visitanteSpinner8;
    @FXML private Spinner visitanteSpinner9;

    private ArrayList<Spinner> setsLocal = new ArrayList<>();
    private ArrayList<Spinner> setsVisitante = new ArrayList<>();

    @Override
    public void inicializar() {
        inicializacionBasica();
        if(!partido.getResultados().isEmpty()){
            cargarResultadoAnterior();
        }
    }

    @Override
    public void inicializar(String mensaje){
        idPartidoClickeado = Integer.parseInt(mensaje);
        inicializar();
    }

    private void cargarResultadoAnterior() {
        List<Resultado> resultadosAnteriores = partido.getResultados();
        sePresentoLocalCheckBox.setSelected(resultadosAnteriores.get(0).isJugoLocal());
        sePresentoVisitanteCheckBox.setSelected(resultadosAnteriores.get(0).isJugoVisitante());
        for(int i=0;i<cantSets;i++){
            setsLocal.get(i).getValueFactory().setValue(resultadosAnteriores.get(i).getTantosEquipoLocal());
            setsVisitante.get(i).getValueFactory().setValue(resultadosAnteriores.get(i).getTantosEquipoVisitante());
        }
        checkBoxClicked();
    }

    private void inicializacionBasica() {
        idCompetencia = (Integer) myController.getControladorAnterior().mensajeControladorAnterior();
        partido = gestorCompetencia.buscarPartidoPorId(idPartidoClickeado);
        sePresentoLocalCheckBox.setText(partido.getLocal().getNombre());
        sePresentoVisitanteCheckBox.setText(partido.getVisitante().getNombre());
        sePresentoLocalCheckBox.setSelected(false);
        sePresentoVisitanteCheckBox.setSelected(false);
        cantSets= gestorCompetencia.buscarCompetenciaPorId(idCompetencia).getCantidadDeSets();
        cargarListaSpinners();
        prepararSpinners();
        error1.setVisible(false);
    }

    private void prepararSpinners() {
        for(int i=0;i<9;i++){
            if(i>=cantSets){
                setsLocal.get(i).setVisible(false);
                setsVisitante.get(i).setVisible(false);
                setsLocal.get(i).setDisable(true);
                setsVisitante.get(i).setDisable(true);
            }
            else{
                setsLocal.get(i).setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
                setsVisitante.get(i).setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
                setsLocal.get(i).valueProperty().removeListener(listenerSpinner);
                setsVisitante.get(i).valueProperty().removeListener(listenerSpinner);
                setsLocal.get(i).valueProperty().addListener(listenerSpinner);
                setsVisitante.get(i).valueProperty().addListener(listenerSpinner);
                setsLocal.get(i).setDisable(true);
                setsVisitante.get(i).setDisable(true);
                setsLocal.get(i).setVisible(true);
                setsVisitante.get(i).setVisible(true);
            }
        }
    }

    private void cargarListaSpinners() {
        setsLocal.add(localSpinner1);
        setsLocal.add(localSpinner2);
        setsLocal.add(localSpinner3);
        setsLocal.add(localSpinner4);
        setsLocal.add(localSpinner5);
        setsLocal.add(localSpinner6);
        setsLocal.add(localSpinner7);
        setsLocal.add(localSpinner8);
        setsLocal.add(localSpinner9);
        setsVisitante.add(visitanteSpinner1);
        setsVisitante.add(visitanteSpinner2);
        setsVisitante.add(visitanteSpinner3);
        setsVisitante.add(visitanteSpinner4);
        setsVisitante.add(visitanteSpinner5);
        setsVisitante.add(visitanteSpinner6);
        setsVisitante.add(visitanteSpinner7);
        setsVisitante.add(visitanteSpinner8);
        setsVisitante.add(visitanteSpinner9);
    }

    @Override
    public Object mensajeControladorAnterior(){ return idCompetencia; }

    public void cancelar(ActionEvent actionEvent){
        volver();
    }

    public void aceptar(ActionEvent actionEvent){
        if(!sePresentoLocalCheckBox.isSelected() && !sePresentoVisitanteCheckBox.isSelected()){
            error1.setText("Alguno de los participantes tiene que haberse presentado");
            error1.setVisible(true);
        }
        else if(!error1.isVisible()){
            ResultadoSetDTO resultadoSetDTO = new ResultadoSetDTO();
            cargarResultadoDTO(resultadoSetDTO);
            if(huboCambios(resultadoSetDTO,partido.getResultados())){
                gestorCompetencia.cargarResultado(resultadoSetDTO);
            }
            volver();
        }
    }

    private boolean huboCambios(ResultadoSetDTO resultadoSetDTO, List<Resultado> resultados) {
        if(resultados.isEmpty()){
            return true;
        }
        if(resultados.get(0).isJugoVisitante() != resultadoSetDTO.isSePresentoVisitante()){
            return true;
        }
        if(resultados.get(0).isJugoLocal() != resultadoSetDTO.isSePresentoLocal()){
            return true;
        }
        for(int i=0; i < resultados.size();i++){
            if(resultados.get(i).getTantosEquipoLocal() != resultadoSetDTO.getTantosLocal()[i]){
                return true;
            }
            if(resultados.get(i).getTantosEquipoVisitante() != resultadoSetDTO.getTantosVisitante()[i]){
                return true;
            }
        }
        return false;
    }

    private void volver() {
        ControlledScreen anterior = myController.getControladorAnterior();
        myController.setControladorAnterior(this);
        Stage modal = (Stage)okButton.getScene().getWindow();
        modal.close();
        anterior.inicializar();
    }

    private void cargarResultadoDTO(ResultadoSetDTO resultadoSetDTO) {
        boolean jugoLocal = sePresentoLocalCheckBox.isSelected();
        boolean jugoVisitante = sePresentoVisitanteCheckBox.isSelected();
        resultadoSetDTO.setSePresentoLocal(jugoLocal);
        resultadoSetDTO.setSePresentoVisitante(jugoVisitante);
        resultadoSetDTO.setIdCompetencia(idCompetencia);
        resultadoSetDTO.setIdPartido(idPartidoClickeado);
        int[] tantosLocal = new int[9];
        int[] tantosVisitante = new int[9];
        for(int i=0;i<9;i++){
            if(setsLocal.get(i).isDisabled() && setsVisitante.get(i).isDisabled()) {
                if (jugoLocal && !jugoVisitante && i < cantSets/2+1) {
                    tantosLocal[i] = 1;
                    tantosVisitante[i] = 0;
                }
                else if(!jugoLocal && jugoVisitante && i < cantSets/2+1){
                    tantosLocal[i] = 0;
                    tantosVisitante[i] = 1;
                }
                else{
                    tantosLocal[i] = 0;
                    tantosVisitante[i] = 0;
                }
            }
            else{
                tantosLocal[i]=(Integer)setsLocal.get(i).getValue();
                tantosVisitante[i]=(Integer)setsVisitante.get(i).getValue();
            }
        }
        resultadoSetDTO.setTantosLocal(tantosLocal);
        resultadoSetDTO.setTantosVisitante(tantosVisitante);
    }

    public void spinnerChange(){
        habilitarSpinnersCorrespondientes();
        if(!setsLocal.get(cantSets-1).isDisabled() && !setsVisitante.get(cantSets-1).isDisabled()){
            int tantosLocal = (Integer)setsLocal.get(cantSets-1).getValue();
            int tantosVisitante = (Integer)setsVisitante.get(cantSets-1).getValue();
            if(tantosLocal == tantosVisitante){
                error1.setText("No se puede empatar un set");
                error1.setVisible(true);
            }
            else{
                error1.setVisible(false);
            }
        }
    }

    public void checkBoxClicked(ActionEvent actionEvent){
        checkBoxClicked();
    }

    private void checkBoxClicked() {
        if(sePresentoLocalCheckBox.isSelected() && sePresentoVisitanteCheckBox.isSelected()){
            setsLocal.get(0).setDisable(false);
            setsVisitante.get(0).setDisable(false);
            spinnerChange();
        }
        else{
            for(int i=0;i<cantSets;i++){
                setsLocal.get(i).setDisable(true);
                setsVisitante.get(i).setDisable(true);
            }
            error1.setVisible(false);
        }
    }

    private void habilitarSpinnersCorrespondientes() {
        int cantSetsGanadosLocal = 0;
        int cantSetsGanadosVisitante = 0;
        for(int i=0;i<cantSets-1;i++){
            int tantosLocal = (Integer)setsLocal.get(i).getValue();
            int tantosVisitante = (Integer)setsVisitante.get(i).getValue();
            if(setsLocal.get(i).isDisabled() || setsVisitante.get(i).isDisabled()){
                setsLocal.get(i+1).setDisable(true);
                setsVisitante.get(i+1).setDisable(true);
            }
            else{
                if(tantosLocal>tantosVisitante) cantSetsGanadosLocal ++;
                if(tantosLocal<tantosVisitante) cantSetsGanadosVisitante++;
                boolean yaGanoLocal = cantSetsGanadosLocal == cantSets/2+1;
                boolean yaGanoVisitante = cantSetsGanadosVisitante == cantSets/2+1;
                if(tantosLocal == tantosVisitante){
                    setsLocal.get(i+1).setDisable(true);
                    setsVisitante.get(i+1).setDisable(true);
                    error1.setText("No se puede empatar un set");
                    error1.setVisible(true);
                }
                else if (yaGanoLocal || yaGanoVisitante){
                    error1.setVisible(false);
                    setsLocal.get(i+1).setDisable(true);
                    setsVisitante.get(i+1).setDisable(true);
                }
                else{
                    setsLocal.get(i+1).setDisable(false);
                    setsVisitante.get(i+1).setDisable(false);
                }
            }
        }
    }
}

