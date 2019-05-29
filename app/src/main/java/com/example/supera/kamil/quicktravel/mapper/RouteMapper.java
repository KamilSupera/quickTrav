package com.example.supera.kamil.quicktravel.mapper;

import com.example.supera.kamil.quicktravel.firebase.mapper.FirebaseMapper;
import com.example.supera.kamil.quicktravel.models.Route;
import com.google.firebase.database.DataSnapshot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/***************************************************************************
 *                                                                         *
 * The most important mapper in this application and firebase connections. *
 * It maps for us all values from routes reference to {@link Route} model  *
 * and all other models used to store information from firebase            *
 * with help of other mappers to devide some mapping instead of using      *
 * one mapper to do everything.                                            *
 *                                                                         *
 ***************************************************************************/
public class RouteMapper extends FirebaseMapper<Route> {

    @Override
    public List<Route> mapList(DataSnapshot dataSnapshot) {
        List<Route> routes = new ArrayList<>();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Route route = new Route();
            route.setName(ds.getKey());

            for (DataSnapshot ds2 : ds.getChildren()) {
                try {
                    // So instead making enum(I can't send parameters to it).
                    // I did same thing we make in DecentWork. Whit calling methods by they names.
                    // Now the names of those methods are the same like keys from routes db reference.
                    // We have there 5 keys right now(if new key is added remember to add new method)
                    // which are: stops, comments, firms, maxTimeOfRide, and totalLength.
                    Method method = getClass().getDeclaredMethod(ds2.getKey(), Route.class, DataSnapshot.class);
                    method.setAccessible(true);
                    method.invoke(this, route, ds2);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            routes.add(route);
        }

        return routes;
    }

    @Override
    public List<Route> mapList(Iterable<DataSnapshot> dataSnapshot) {
        return null;
    }

    /**
     *
     *---------------------------------------------------------------*
     * So here are those methods which are called when corresponding *
     * with they name key is called. Every method has to add         *
     * {@link Route} and {@link DataSnapshot} parameters or          *
     * {@link NoSuchMethodException} will be called.                 *
     *---------------------------------------------------------------*
     *
     * @param route - Route object containing all information about the route.
     * @param dataSnapshot - Data from Firebase db in key reference e.g. stops or comments.
     */
    private void stops(Route route, DataSnapshot dataSnapshot) {
        route.setStops(new StopMapper().mapList(dataSnapshot.getChildren()));
    }

    private void maxTimeOfRide(Route route, DataSnapshot dataSnapshot) {
        route.setMaxTimeOfRide(Double.parseDouble(dataSnapshot.getValue().toString()));
    }

    private void totalLength(Route route, DataSnapshot dataSnapshot) {
        route.setTotalLength(dataSnapshot.getValue().toString());
    }

    private void firms(Route route, DataSnapshot dataSnapshot) {
        route.setFirms(new FirmMapper().mapList(dataSnapshot.getChildren()));
    }

    private void rating(Route route, DataSnapshot dataSnapshot) {
        route.setRating(Float.parseFloat(dataSnapshot.getValue().toString()));
    }

    private void votes(Route route, DataSnapshot dataSnapshot) {
        route.setVotes(Integer.parseInt(dataSnapshot.getValue().toString()));
    }
}
