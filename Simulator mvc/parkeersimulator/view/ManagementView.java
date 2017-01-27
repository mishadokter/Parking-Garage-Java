package parkeersimulator.view;

import java.io.IOException;
/**
 * Created by Misha on 26-1-2017.
 */
public class ManagementView extends DefaultView {

    public ManagementView() throws IOException {
        super();
    }

    @Override
    public String getLayoutFile() {
        return "management.fxml";
    }

    @Override
    public String getTitle() {
        return "Management";
    }
}
