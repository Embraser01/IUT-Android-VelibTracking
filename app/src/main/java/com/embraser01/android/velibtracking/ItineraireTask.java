package com.embraser01.android.velibtracking;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class ItineraireTask extends AsyncTask<Void, Integer, Boolean> {

    private Context context;
    private GoogleMap gMap;
    private LatLng editDepart;
    private LatLng editArrivee;
    private final ArrayList<LatLng> lstLatLng = new ArrayList<>();

    /**
     * Constructeur.
     *
     * @param context
     * @param gMap
     * @param editDepart
     * @param editArrivee
     */
    public ItineraireTask(final Context context, final GoogleMap gMap, final LatLng editDepart, final LatLng editArrivee) {
        this.context = context;
        this.gMap = gMap;
        this.editDepart = editDepart;
        this.editArrivee = editArrivee;
    }

    /***
     * {@inheritDoc}
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            //Construction de l'url à appeler
            final StringBuilder url = new StringBuilder("http://maps.googleapis.com/maps/api/directions/xml?sensor=false&language=fr&mode=b");
            url.append("&origin=");
            url.append(editDepart.latitude).append(",").append(editDepart.longitude);
            url.append("&destination=");
            url.append(editArrivee.latitude).append(",").append(editArrivee.longitude);

            //Appel du web service
            final InputStream stream = new URL(url.toString()).openStream();

            //Traitement des données
            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setIgnoringComments(true);

            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            final Document document = documentBuilder.parse(stream);
            document.getDocumentElement().normalize();

            //On récupère d'abord le status de la requête
            final String status = document.getElementsByTagName("status").item(0).getTextContent();
            if (!"OK".equals(status)) {
                return false;
            }

            //On récupère les steps
            final Element elementLeg = (Element) document.getElementsByTagName("leg").item(0);
            final NodeList nodeListStep = elementLeg.getElementsByTagName("step");
            final int length = nodeListStep.getLength();

            for (int i = 0; i < length; i++) {
                final Node nodeStep = nodeListStep.item(i);

                if (nodeStep.getNodeType() == Node.ELEMENT_NODE) {
                    final Element elementStep = (Element) nodeStep;

                    //On décode les points du XML
                    decodePolylines(elementStep.getElementsByTagName("points").item(0).getTextContent());
                }
            }

            return true;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * Méthode qui décode les points en latitude et longitudes ==> http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     *
     * @param encodedPoints
     */
    private void decodePolylines(final String encodedPoints) {
        int index = 0;
        int lat = 0, lng = 0;

        while (index < encodedPoints.length()) {
            int b, shift = 0, result = 0;

            do {
                b = encodedPoints.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;

            do {
                b = encodedPoints.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            lstLatLng.add(new LatLng((double) lat / 1E5, (double) lng / 1E5));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(final Boolean result) {
        if (!result) {
            Toast.makeText(context, R.string.go_to_fail_itinary, Toast.LENGTH_SHORT).show();
        } else {
            //On déclare le polyline, c'est-à-dire le trait (ici bleu) que l'on ajoute sur la carte pour tracer l'itinéraire
            final PolylineOptions polylines = new PolylineOptions();
            polylines.color(Color.BLUE);

            //On construit le polyline
            for (final LatLng latLng : lstLatLng) {
                polylines.add(latLng);
            }

            LatLngBounds lngBounds = new LatLngBounds.Builder().include(lstLatLng.get(0)).include(lstLatLng.get(lstLatLng.size() - 1)).build();

            gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(lngBounds, 25));
            gMap.addPolyline(polylines);
        }
    }
}
