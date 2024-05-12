package ru.practicum.shareit.booking.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingInputDTO;
import ru.practicum.shareit.booking.enumeration.State;
import ru.practicum.shareit.client.BaseClient;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(long userId, BookingInputDTO inputDTO) {
        String url = "";
        return post(url, userId, inputDTO);
    }

    public ResponseEntity<Object> getBookingById(long userId, Long bookingId) {
        String url = "/" + bookingId;
        return get(url, userId);
    }

    public ResponseEntity<Object> getAllBookingsAtBooker(long userId, State state, Integer from, Integer size) {
        String url = String.format("?state=%s&from=%d&size=%d", state.name(), from, size);

        return get(url, userId);
    }

    public ResponseEntity<Object> getAllBookingsAtOwner(long userId, State state, Integer from, Integer size) {
        String url = String.format("/owner?state=%s&from=%d&size=%d", state.name(), from, size);

        return get(url, userId);
    }

    public ResponseEntity<Object> updateBooking(long userId, long bookingId, boolean approved) {
        String url = String.format("/%d?approved=%s", bookingId, approved);

        return patch(url, userId);
    }
}
