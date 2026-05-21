package com.t20simulator.service;

import com.t20simulator.dao.VenueDAO;
import com.t20simulator.model.Venue;

import java.sql.SQLException;
import java.util.List;

/**
 * Coordinates venue-related actions for the Swing UI.
 */
public class VenueService {
    private final VenueDAO venueDAO = new VenueDAO();

    public void addVenue(Venue venue) throws SQLException {
        venueDAO.insert(venue);
    }

    public List<Venue> getAllVenues() throws SQLException {
        return venueDAO.getAll();
    }
}
