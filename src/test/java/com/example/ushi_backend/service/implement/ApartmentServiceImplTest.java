package com.example.ushi_backend.service.implement;

import com.example.ushi_backend.dto.response.FavoriteToggleResponse;
import com.example.ushi_backend.entity.FavoritePostEntity;
import com.example.ushi_backend.entity.PostEntity;
import com.example.ushi_backend.entity.RecentlyViewedPostEntity;
import com.example.ushi_backend.entity.UserEntity;
import com.example.ushi_backend.exception.UnauthorizedException;
import com.example.ushi_backend.repository.FavoritePostRepository;
import com.example.ushi_backend.repository.PostAmenityRepository;
import com.example.ushi_backend.repository.PostImageRepository;
import com.example.ushi_backend.repository.PostRepository;
import com.example.ushi_backend.repository.PostServicePriceRepository;
import com.example.ushi_backend.repository.RecentlyViewedPostRepository;
import com.example.ushi_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApartmentServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private FavoritePostRepository favoritePostRepository;

    @Mock
    private RecentlyViewedPostRepository recentlyViewedPostRepository;

    @Mock
    private PostImageRepository postImageRepository;

    @Mock
    private PostAmenityRepository postAmenityRepository;

    @Mock
    private PostServicePriceRepository postServicePriceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApartmentServiceImpl apartmentService;

    @Test
    void toggleFavorite_shouldCreateFavoriteAndIncreaseCount() {
        UserEntity user = new UserEntity();
        user.setId(11L);

        PostEntity post = new PostEntity();
        post.setId(22L);
        post.setFavoriteCount(2L);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(postRepository.findById(22L)).thenReturn(Optional.of(post));
        when(favoritePostRepository.findByUserIdAndPost_Id(11L, 22L)).thenReturn(Optional.empty());

        FavoriteToggleResponse response = apartmentService.toggleFavorite(22L, "user@example.com");

        assertTrue(response.liked());
        assertEquals(3L, post.getFavoriteCount());
        verify(favoritePostRepository).save(any(FavoritePostEntity.class));
        verify(postRepository).save(post);
    }

    @Test
    void markAsRecentlyViewed_shouldUpdateExistingRecordAndIncreaseViewCount() {
        UserEntity user = new UserEntity();
        user.setId(11L);

        PostEntity post = new PostEntity();
        post.setId(22L);
        post.setViewCount(7L);

        RecentlyViewedPostEntity existing = new RecentlyViewedPostEntity();
        existing.setId(99L);
        existing.setUserId(11L);
        existing.setPost(post);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(postRepository.findById(22L)).thenReturn(Optional.of(post));
        when(recentlyViewedPostRepository.findByUserIdAndPost_Id(11L, 22L)).thenReturn(Optional.of(existing));

        apartmentService.markAsRecentlyViewed(22L, "user@example.com");

        assertEquals(8L, post.getViewCount());
        verify(recentlyViewedPostRepository).save(existing);
        verify(postRepository).save(post);
    }

    @Test
    void toggleFavorite_shouldRejectWhenUserIsAnonymous() {
        assertThrows(UnauthorizedException.class, () -> apartmentService.toggleFavorite(22L, null));
        verify(postRepository, never()).findById(any());
    }
}
