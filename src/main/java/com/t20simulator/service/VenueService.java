package com.t20simulator.service;

import com.t20simulator.dao.VenueDAO;
import com.t20simulator.model.Venue;

import java.sql.SQLException;
import java.util.List;

/**
 * Coordinates venue-related actions for the Swing UI.
 * This keeps venue database access out of Swing classes.
 */
public class VenueService {
    private final VenueDAO venueDAO = new VenueDAO();

    public void addVenue(Venue venue) throws SQLException {
        // Forward venue model to DAO for INSERT query.
        venueDAO.insert(venue);
    }

    public List<Venue> getAllVenues() throws SQLException {
        // Used by Match Entry venue combo box.
        return venueDAO.getAll();
    }
}
